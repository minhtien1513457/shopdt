import { Component, ViewChild, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material/sidenav';
import { PlatformLocation, Location } from '@angular/common';
import { JwtService } from './shared/services/jwt.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'shopdtfe';
  constructor(
    private jwt: JwtService,
    router: Router,
    location: PlatformLocation,
    local: Location,
    ) {
      location.onPopState(() => {
        if (location.pathname === "/login") {
          this.jwt.clearStorage();
        }
      });
    
      if (jwt.getTicket() && local.path() === "/login") {
        router.navigate([`/layout/dashboard`]);
      }
    
  }
 
}


package com.iaas.vsr.dbaas.service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.trove.Backup;
import org.openstack4j.model.trove.Instance;
import org.openstack4j.model.trove.InstanceCreate;
import org.openstack4j.model.trove.builder.InstanceCreateBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.MutableTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaas.api.CLSClient;
import com.iaas.api.ClientFactory;
import com.iaas.api.ServiceType;
import com.iaas.api.builder.scheduler.UserSchedulerRequestBuilder;
import com.iaas.api.core.ClientThreadLocal;
import com.iaas.api.exception.SchedulerException;
import com.iaas.api.scheduler.request.UserSchedulerRequest;
import com.iaas.api.scheduler.result.SdlResult;
import com.iaas.sc.client.SCClient;
import com.iaas.sc.client.component.keystone.model.BaseResponse;
import com.iaas.sc.client.component.trove.model.BackupDbaas;
import com.iaas.sc.client.component.trove.request.CreateBackupRequest;
import com.iaas.sc.client.component.trove.request.DeleteBackupRequest;
import com.iaas.vsr.config.VsrApiConfig;
import com.iaas.vsr.config.VsrDBApiConfig;
import com.iaas.vsr.core.model.DbaasBackup;
import com.iaas.vsr.core.model.DbaasBackupScheduler;
import com.iaas.vsr.core.model.DbaasInstance;
import com.iaas.vsr.core.model.OpsUser;
import com.iaas.vsr.dbaas.DBaaSException;
import com.iaas.vsr.dbaas.model.AddressInfo;
import com.iaas.vsr.dbaas.model.BackupAutoType;
import com.iaas.vsr.dbaas.model.BackupInfo;
import com.iaas.vsr.dbaas.model.BackupType;
import com.iaas.vsr.dbaas.model.ConfigurationInfo;
import com.iaas.vsr.dbaas.model.DbInstanceInfo;
import com.iaas.vsr.dbaas.model.TroveStatus;
import com.iaas.vsr.dbaas.request.BackupRequest;
import com.iaas.vsr.dbaas.request.RestoreRequest;
import com.iaas.vsr.dbaas.validate.TroveValidator;
import com.iaas.vsr.helper.DbaasConstant;
import com.iaas.vsr.helper.LogUtil;
import com.iaas.vsr.respone.entity.SecuritygroupEntity;
import com.iaas.vsr.service.IOps4jService;
import com.iaas.vsr.service.InterfaceProjectService;
import com.iaas.vsr.service.impl.TransactionServiceImpl;
import com.iaas.vsr.service.impl.VsrServiceRef;
import com.iaas.vsr.util.Status;
import com.ird.log.entity.RootEntity;

public class TroveBackupServiceImpl extends TransactionServiceImpl implements TroveBackupService {
    private static final Logger LOG = Logger.getLogger(TroveBackupServiceImpl.class);

    @Autowired(required = false)
    private IOps4jService ops4jService;
    
    @Autowired
    InterfaceProjectService interfaceProjectService;

    @Autowired
    private TroveInstanceService instanceService;

    @Autowired
    private TroveValidator validator;

    @Autowired
    private TrovePackageService packageService;

    @Autowired
    private TroveConfigurationService configurationService;
    
    @Autowired
    private DBAASRedisService dbaasRedisService;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public List<BackupInfo> getBackups(OpsUser opsUser, String projectId) throws DBaaSException {
        return getBackups(opsUser,projectId,null);    
    }
    
    @Override
    public List<BackupInfo> getBackups(OpsUser opsUser, String projectId, Integer engineGroup) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("getBackups").reqProp("projectId", projectId);

        try {
        	//call sns_api
        	SCClient.SCClientV1 scClientV1 = VsrServiceRef.INSTANCE.scClientService.getSCClient();
            Integer projectIdInt = interfaceProjectService.getByUuid(projectId).getProjectId();
            BaseResponse<BackupDbaas> backupInstance = scClientV1.trove().backup().listByProject(projectIdInt);
//            OSClient.OSClientV3 osClient = ops4jService.getOpsClient(opsUser, projectId);
//            final List<? extends Backup> backupInstance = osClient.trove().backupService().list();
            if (backupInstance != null && !backupInstance.getItems().isEmpty()) {
                beginTransaction();

                Criterion criteria;
                criteria = Restrictions.conjunction()
                        .add(Restrictions.eq("opsUser", opsUser.getOpsUsername()))
                        .add(Restrictions.eq("deleted", false))
                        .add(Restrictions.or(Restrictions.eq("engineGroup", engineGroup), Restrictions.isNull("engineGroup")));

                final List<DbaasBackup> backups = findCollections(DbaasBackup.class, criteria, false);
                final Map<String, DbaasBackup> idBackups = backups.stream().collect(Collectors.toMap(DbaasBackup::getBackupId, e -> e));

                final Set<String> ids = backups.stream().map(DbaasBackup::getInstanceId).collect(Collectors.toSet());
                final Set<String> hideIds = backups.parallelStream().filter(e -> e.getHided() != null)
                        .filter(DbaasBackup::getHided).map(DbaasBackup::getBackupId)
                        .collect(Collectors.toSet());

                List<DbaasInstance> instances;
                if (ids.isEmpty()) {
                    instances = Collections.emptyList();
                } else {
                    Criterion inCriteria = Restrictions.conjunction()
                            .add(Restrictions.in("instanceId", ids))
                            .add(Restrictions.isNull("dbaasInstanceNext"));

                    instances = findCollections(DbaasInstance.class, inCriteria, false);
                }

                commitTransaction();

                final Map<String, String> inName = instances.stream().collect(Collectors.toMap(DbaasInstance::getInstanceId, DbaasInstance::getName));
                final List<BackupInfo> listResult = backupInstance.getItems().parallelStream().map(e -> {
                    final BackupInfo rs = new BackupInfo(e);
                    final DbaasBackup bk = idBackups.get(rs.getId());
                    if (bk != null) {
                        rs.setBackupType(bk.getBackupType());
                        rs.setType(bk.getType());
                        rs.setInstanceName(inName.get(bk.getInstanceId()));
                        rs.setCreated(bk.getCreated() != null ? bk.getCreated().toString() : null);
                        if (rs.getParent() != null) {
                            final DbaasBackup parent = idBackups.get(rs.getParent());
                            if (parent != null) {
                                rs.setParentName(parent.getBackupName());
                            }
                        }
                    } else {
                        LOG.warn("Cannot find backup [" + e.getBackupId() + "] in DB");
                    }
                    return rs;
                }).filter(e -> idBackups.containsKey(e.getId()))
                        .filter(e -> !hideIds.contains(e.getId()))
                        .collect(Collectors.toList());
                log.success();
                    return listResult;
            } else {
                log.success();
                return Collections.emptyList();
            }
        } catch (Exception ex) {
            rollbackTransaction();
            log.exp(ex).fail();
            LOG.error("Error when getBackups", ex);
            throw new DBaaSException("Cannot get all backup", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }

    }

    @Override
    public List<BackupInfo> getBackups(OSClient osClient, String projectId, String dbInstanceId) {
        //final List<? extends Backup> backupInstance = osClient.trove().backupService().instance(dbInstanceId);
        BaseResponse<BackupDbaas> backupInstance = null;
    	try {
    	 //call sns_api
    	 SCClient.SCClientV1 scClientV1 = VsrServiceRef.INSTANCE.scClientService.getSCClient();
         Integer projectIdInt = interfaceProjectService.getByUuid(projectId).getProjectId();
         backupInstance = scClientV1.trove().backup().listByInstance(projectIdInt, Integer.parseInt(dbInstanceId));
    	} catch (Exception e) {
            LOG.error("Error when getbackup", e);
    	}
        if (backupInstance != null && !backupInstance.getItems().isEmpty()) {
            Criterion criteria = Restrictions.conjunction()
                    .add(Restrictions.eq("instanceId", dbInstanceId))
                    .add(Restrictions.eq("deleted", false));
            final List<DbaasBackup> backups = findCollections(DbaasBackup.class, criteria);
            final Map<String, DbaasBackup> idBackups = backups.stream().collect(Collectors.toMap(DbaasBackup::getBackupId, e -> e));
            final Set<String> hideIds = backups.parallelStream().filter(e -> e.getHided() != null)
                    .filter(DbaasBackup::getHided).map(DbaasBackup::getBackupId)
                    .collect(Collectors.toSet());

            return backupInstance.getItems().parallelStream().map(e -> {
                final BackupInfo rs = new BackupInfo(e);
                final DbaasBackup bk = idBackups.get(rs.getId());
                if (bk != null) {
                    rs.setBackupType(bk.getBackupType());
                    rs.setType(bk.getType());
                    rs.setCreated(bk.getCreated() != null ? bk.getCreated().toString() : null);
                    if (rs.getParent() != null) {
                        final DbaasBackup parent = idBackups.get(rs.getParent());
                        if (parent != null) {
                            rs.setParentName(parent.getBackupName());
                        }
                    }
                } else {
                    LOG.warn("Cannot find backup [" + e.getBackupId() + "] in DB");
                }
                return rs;
            }).filter(e -> idBackups.containsKey(e.getId()))
                    .filter(e -> !hideIds.contains(e.getId())).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<BackupInfo> getBackup(OSClient osClient, String projectId, String backupId) throws DBaaSException {
//        final Backup backup = osClient.trove().backupService().get(backupId);
    	//call sns_api
    	BaseResponse<BackupDbaas> backup = null;
    	try {
    	 SCClient.SCClientV1 scClientV1 = VsrServiceRef.INSTANCE.scClientService.getSCClient();
         Integer projectIdInt = interfaceProjectService.getByUuid(projectId).getProjectId();
         backup = scClientV1.trove().backup().getDetail(projectIdInt, Integer.parseInt(backupId));
    	} catch (Exception e) {
            LOG.error("Error when getbackup", e);
    	}
    	
        if (backup != null) {
            try {
                final BackupInfo result = new BackupInfo(backup.getItems().get(0));

                beginTransaction();

                final DbaasBackup bk = find(DbaasBackup.class, Restrictions.eq("backupId", backupId), false);
                if (bk != null) {
                    result.setBackupType(bk.getBackupType());
                    result.setType(bk.getType());

                    final DbaasInstance in = find(DbaasInstance.class, Restrictions.eq("dbaasInstanceId", bk.getDbaasInstanceId()), false);

                    result.setConfigId(in.getConfigId());
                    if (in.getConfigId() != null) {
                        final Optional<ConfigurationInfo> config = configurationService.getPartialConfig(osClient, projectId, in.getConfigId());
                        if (config.isPresent()) {
                            result.setConfigName(config.get().getName());
                        } else {
                            LOG.warn("Cannot found configId " + in.getConfigId());
                        }
                    }
                    if (!StringUtils.isEmpty(result.getParent())) {
                        final DbaasBackup parent = find(DbaasBackup.class, Restrictions.eq("backupId", result.getParent()), false);

                        result.setParentName(parent.getBackupName());
                    }

                    result.setNetIds(in.getNetwork() == null ? null : Arrays.asList(in.getNetwork().split(",")));
                    result.setUsername(in.getUsername());
                    result.setInstanceName(in.getName());
                    result.setCreated(bk.getCreated() != null ? bk.getCreated().toString() : null);
                    result.setRam(in.getRam());
                    result.setVcpu(in.getVcpus());
                    result.setStorageType(in.getVolumeType());
                    result.setStorageSize(in.getVolumeSize());
                    result.setDatastoreType(in.getDatastoreType());

                    final DbaasBackupScheduler sdl = find(DbaasBackupScheduler.class, Restrictions.eq("backupSchedulerId", in.getBackupSchedulerId()), false);
                    result.setBackupDuration(sdl.getBackupDuration());
                } else {
                    LOG.warn("Cannot get Backup from DB " + backupId);
                }

                commitTransaction();
                return Optional.of(result);
            } catch (Exception ex) {
                LOG.error("Error when getbackup", ex);
                rollbackTransaction();
                throw new DBaaSException("Cannot get backup", ex);
            }

        } else {
            return Optional.empty();
        }
    }

    @Override
    public BackupInfo createBackup(OpsUser opsUser, String projectId, BackupRequest request) throws DBaaSException {
        //validate
        final Map<String, String> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            throw new DBaaSException(errors.toString());
        }

        final Junction criteria = Restrictions.conjunction().add(Restrictions.eq("opsUser", opsUser.getOpsUsername()))
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("type", BackupAutoType.MANUAL.name()));

        final List<DbaasBackup> existBackups = findCollections(DbaasBackup.class, criteria);
        if (existBackups.size() >= VsrDBApiConfig.maxBackupPerUser) {
            throw new DBaaSException(String.format("Quota limit %s backups has been reached", VsrDBApiConfig.maxBackupPerUser));
        }


        final String backupAutoType = BackupAutoType.MANUAL.name();
        return createBackup(opsUser, projectId, request, backupAutoType);

    }
    
    private BackupInfo createBackup(OpsUser opsUser, String projectId, BackupRequest request, String backupAutoType) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("createBackup").reqProp("projectId", projectId).reqProp("request", request);
        try {

            Criterion backupCriteria = Restrictions.conjunction()
                    .add(Restrictions.eq("backupName", request.getName()))
                    .add(Restrictions.eq("opsUser", opsUser.getOpsUsername()))
                    .add(Restrictions.eq("deleted", false));
            final List<DbaasBackup> existBackups = findCollections(DbaasBackup.class, backupCriteria);
            if (!existBackups.isEmpty()) {
                throw new DBaaSException("Backup name must be unique");
            }


//            final BackupCreate createRequest = Builders.trove().backupCreate().instance(request.getInstanceId()).name(request.getName())
//                    .description(request.getDescription())
//                    .parent(request.getParentId())
//                    .incremental(BackupType.valueOf(Integer.toString(request.getIncremental())).getType()).build();

//            OSClient.OSClientV3 osClient = VsrServiceRef.INSTANCE.ops4jService.getOpsClient(opsUser, projectId);
//            final Backup backup = osClient.trove().backupService().create(createRequest);
            SCClient.SCClientV1 scClientV1 = VsrServiceRef.INSTANCE.scClientService.getSCClient();
            Integer projectIdInt = interfaceProjectService.getByUuid(projectId).getProjectId();
            CreateBackupRequest createBackupRequest = CreateBackupRequest.builder()
           		 .projectId(Integer.toString(projectIdInt))
           		 .instanceId(request.getInstanceId())
           		 .name(request.getName())
           		 .description(request.getDescription())
           		 .incremental(BackupType.valueOf(request.getBackupType()).getType())
           		 .parentId(request.getParentId()).build();
            BaseResponse<BackupDbaas> backup = scClientV1.trove().backup().create(createBackupRequest);

            if (backup == null) {
                throw new DBaaSException("Cannot create backup");
            }

            //Save instance to Database
            Criterion criteria = Restrictions.conjunction()
                    .add(Restrictions.eq("instanceId", request.getInstanceId()))
                    .add(Restrictions.isNull("dbaasInstanceNext"));

            final DbaasInstance instanceEntity = find(DbaasInstance.class, criteria);
            final DbaasBackup bkEntity = new DbaasBackup();

            bkEntity.setBackupId(Long.toString(backup.getItems().get(0).getBackupId()));
            bkEntity.setBackupName(request.getName());
            bkEntity.setInstanceId(Long.toString(backup.getItems().get(0).getInstanceId()));
            bkEntity.setDbaasInstanceId(instanceEntity.getDbaasInstanceId());

            bkEntity.setType(backupAutoType);
            bkEntity.setBackupType(request.getBackupType());
            bkEntity.setBackupParent(request.getParentId());
            bkEntity.setDescription(request.getDescription());
            bkEntity.setCreated(new Date());
            bkEntity.setUpdated(bkEntity.getCreated());
            bkEntity.setDeleted(false);
            bkEntity.setOpsUser(instanceEntity.getOpsUser());
            bkEntity.setEngineGroup(request.getEngineGroup());

            save(bkEntity);

            //set time created backup
            backup.getItems().get(0).setCreated(new Date().toString());
            
            //init BackupInfo to return request
            final BackupInfo rs = new BackupInfo(backup.getItems().get(0));
            rs.setInstanceName(instanceEntity.getName());
            rs.setUsername(instanceEntity.getUsername());
            log.success(bkEntity);
            return rs;
        } catch (Exception ex) {
            log.exp(ex).fail();
            if (ex instanceof DBaaSException) {
                throw (DBaaSException) ex;
            }
            throw new DBaaSException("Cannot createBackup", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }

    @Override
    public BackupInfo createBackupAuto(OpsUser opsUser, String projectId, BackupRequest request) throws DBaaSException {
        final String backupAutoType = BackupAutoType.AUTO_DAILY.name();
        return createBackup(opsUser, projectId, request, backupAutoType);
    }

    @Override
    public BackupInfo createBackupFinal(OpsUser opsUser, String projectId, BackupRequest request) throws DBaaSException {
        return createBackup(opsUser, projectId, request, BackupAutoType.MANUAL.name());
    }

    @Override
    public DbInstanceInfo restoreBackup(OpsUser opsUser, String projectId, RestoreRequest request) throws DBaaSException {
        if (request.getEngineGroup().equals(DbaasConstant.EngineGroup.RELATIONAL)) {
            return restoreRelationBackup(opsUser,projectId,request);
        } else if (request.getEngineGroup().equals(DbaasConstant.EngineGroup.MEMCACHE)) {
            return restoreMemcacheBackup(opsUser,projectId,request);
        } else {
            throw new DBaaSException("Invalid engine group");
        }
    }
    
    private DbInstanceInfo restoreMemcacheBackup(OpsUser opsUser, String projectId, RestoreRequest request) throws  DBaaSException {
        Integer volumeSize = instanceService.calculateMemcacheVolumeSize(request.getRam());
        request.setVolumeSize(volumeSize);

        return restoreDbBackup(opsUser,projectId,request);
    }
    
    private DbInstanceInfo restoreRelationBackup(OpsUser opsUser, String projectId, RestoreRequest request) throws  DBaaSException {
        if (request.getFlavorId() == null) {
            final Optional<String> flavorIdOp = packageService.getFlavorId(request.getVcpus(), request.getRam() * 1024);
            final String flavorId = flavorIdOp.orElseThrow(() -> new DBaaSException("Cannot get Flavor"));
            request.setFlavorId(flavorId);
        }
        
        return restoreDbBackup(opsUser,projectId,request);
    }
    
    private DbInstanceInfo restoreDbBackup(OpsUser opsUser, String projectId, RestoreRequest request) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("restoreBackup").reqProp("projectId", projectId)
                .reqProp("backupId", request != null ? request.getBackupId() : "");

        DbaasInstance in = null;
        try {

            //validate
            final Map<String, String> errors = validator.validate(request);
            if (!errors.isEmpty()) {
                throw new DBaaSException(errors.toString());
            }

            Criterion criteria = Restrictions.conjunction()
                    .add(Restrictions.eq("name", request.getName()))
                    .add(Restrictions.isNull("dbaasInstanceNext"))
                    .add(Restrictions.eq("opsUser", opsUser.getOpsUsername()))
                    .add(Restrictions.eq("deleted", false));
            final List<DbaasInstance> existInstance = findCollections(DbaasInstance.class, criteria);
            if (!existInstance.isEmpty()) {
                throw new DBaaSException("DBInstance name must be unique");
            }

            List<String> netId = new LinkedList<>();
            netId.addAll(request.getNetIds());
            netId.addAll(Arrays.asList(VsrDBApiConfig.defaultNetwork));

            OSClient.OSClientV3 osClient = ops4jService.getOpsClient(opsUser, projectId);

            final List<DbInstanceInfo> allInstance = instanceService.getPartialDbInstances(osClient, projectId);
            if (allInstance.size() >= VsrDBApiConfig.maxInstancePerUser) {
                throw new DBaaSException(String.format("Quota limit %s DB Instance has been reached", VsrDBApiConfig.maxInstancePerUser));
            }

            final InstanceCreateBuilder builder = Builders.trove().instanceCreate().name(request.getName())
                    .flavor(request.getFlavorId())
                    .volumeSize(request.getVolumeSize()).volumeType(request.getVolumeType())
                    .datastoreType(request.getDatastoreType()).datastoreVersion(request.getDatastoreVersion())
                    .config(request.getConfigId())
                    .nics(netId.toArray(new String[0]))
                    .backupRef(request.getBackupId());

            final InstanceCreate createRequest = builder.build();

            Instance result = osClient.trove().instanceService().create(createRequest);
            if (result == null) {
                throw new DBaaSException("Cannot call database service");
            }

            //add endTime startTime
            if (StringUtils.isEmpty(request.getStartTime())) {
                request.setStartTime(new Date());
                if (request.getPeriod() != null) {
                    request.setEndTime(DateUtils.addDays(request.getStartTime(), request.getPeriod()*30));
                }
            }

            //Insert DB
            in = new DbaasInstance(request);
            final DbaasBackupScheduler scheduler = new DbaasBackupScheduler();
            try {
                beginTransaction();
                //scheduler
                scheduler.setInstanceId(result.getId());

                if (request.isBackupAuto()) {
                    scheduler.setType(BackupAutoType.AUTO_DAILY.name());
                    scheduler.setBackupTime(request.getBackupTime());
                    scheduler.setBackupDuration(request.getBackupDuration());
                } else {
                    scheduler.setType(BackupAutoType.MANUAL.name());
                }
                scheduler.setUserSchdId(null);
                scheduler.setCreated(new Date());
                scheduler.setUpdated(scheduler.getCreated());
                scheduler.setDeleted(false);

                save(scheduler, false);

                //instance
                in.setInstanceId(scheduler.getInstanceId());
                in.setPort(DbaasConstant.Port.getPortValue(request.getDatastoreType()));
                in.setOpsUser(opsUser.getOpsUsername());
                in.setCreated(new Date());
                in.setUpdated(in.getCreated());
                in.setDeleted(false);
                in.setStatus(TroveStatus.BUILDING.name());
                in.setBackupSchedulerId(scheduler.getBackupSchedulerId());
                in.setProjectId(opsUser.getOpsProjectId());
                in.setCost(request.getCost());
                in.setEngineGroup(request.getEngineGroup());

                save(in, false);

                commitTransaction();
                request.getExtra().put("instanceId",in.getInstanceId());
            } catch (Exception ex) {
                rollbackTransaction();
                throw new DBaaSException("Cannot save DbInstance", ex);
            }

            Server server = instanceService.addMetadata(osClient, projectId, result.getId());

            if (request.isPublicAccess()) {
                instanceService.addFloatingIP(opsUser, projectId, result.getId());
                server = ops4jService.getServer(osClient, server.getId());
            }

            Integer userSchdId = null;
            if (request.isBackupAuto()) {
                userSchdId = createSchedulerBackupDaily(opsUser, projectId, result.getId(), request.getBackupTime(), request.getBackupDuration(),request.getEngineGroup());
            }

            // Get nova info
            final Map<String, List<AddressInfo>> address = instanceService.getAddress(server);

            //Get SecGroup Id
            final SecuritygroupEntity sge = instanceService.getSecGroup(osClient, result, server);

            //Backup scheduler
            try {
                beginTransaction();

                final DbaasBackup backup = find(DbaasBackup.class, Restrictions.eq("backupId", request.getBackupId()));
                final DbaasInstance instance = find(DbaasInstance.class, Restrictions.eq("dbaasInstanceId", backup.getDbaasInstanceId()));


                beginTransaction();

                scheduler.setUserSchdId(userSchdId);

                update(scheduler, false);

                //instance
                in.setSecGroupId(sge.getId());
                in.setSecGroupName(sge.getName());
                in.setAddress(objectMapper.writeValueAsString(address));
                in.setBackupSchedulerId(scheduler.getBackupSchedulerId());
                in.setUpdated(scheduler.getCreated());
                in.setStatus(null);
                in.setServerId(server.getId());

                in.setUsername(instance.getUsername());
                update(in, false);

                commitTransaction();
            } catch (Exception ex) {
                rollbackTransaction();
                throw new DBaaSException(ex.getMessage(), ex);
            }


            final DbInstanceInfo rs = new DbInstanceInfo(result);
            rs.setId(in.getInstanceId());
            rs.setName(in.getName());
            rs.setRam(in.getRam());
            rs.setVcpus(in.getVcpus());
            rs.setVolumeSize(request.getVolumeSize());
            rs.setVolumeType(request.getVolumeType());
            rs.setStartTime(in.getStartTime());
            rs.setEndTime(in.getEndTime());
            rs.setPeriod(in.getPeriod());
            rs.setServerId(server.getId());
            rs.setZoneId(request.getZoneId());
            rs.setVolumeId(instanceService.getVolumeId(projectId, rs.getId()).orElseThrow(() -> new DBaaSException("Cannot get volume id")));

            log.success();
            return rs;
        } catch (Exception ex) {
            LOG.error("Cannot Restore DBInstance", ex);
            try {
                if (in != null) {
                    in.setStatus(TroveStatus.INTERNAL_ERROR.name());
                    update(in);
                }
            } catch (Exception e) {
                LOG.error(ex);
            }
            log.exp(ex).fail();
            if (ex instanceof DBaaSException) {
                throw (DBaaSException) ex;
            }
            throw new DBaaSException("Cannot createBackup", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }

    }

    /**
     * Delete backup full will delete all backup full and incremental
     *
     * @param osClient
     * @param projectId
     * @param backupId
     * @throws DBaaSException
     */
    @Override
    public void deleteBackup(OSClient osClient, String projectId, String backupId) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("deleteBackup").reqProp("projectId", projectId)
                .reqProp("backupId", backupId);        
        try {
            beginTransaction();
            final DbaasBackup deleteBackup = find(DbaasBackup.class, Restrictions.eq("backupId", backupId), false);
            final Junction child = Restrictions.conjunction().add(Restrictions.eq("backupParent", backupId)).add(Restrictions.eq("deleted", false));
            final List<DbaasBackup> childBackups = findCollections(DbaasBackup.class, child, false);

            if (BackupAutoType.MANUAL.name().equals(deleteBackup.getType())) {
                childBackups.add(deleteBackup);
                deleteBackup(osClient, childBackups, false, projectId);
            } else if (BackupAutoType.AUTO_DAILY.name().equals(deleteBackup.getType())) {
                Criterion criteria = Restrictions.conjunction()
                        .add(Restrictions.eq("instanceId", deleteBackup.getInstanceId()))
                        .add(Restrictions.isNull("dbaasInstanceNext"));
                final DbaasInstance instance = find(DbaasInstance.class, criteria, false);
                if (instance.getDeleted()) {
                    LOG.info("DBInstance " + instance.getInstanceId() + " is deleted! Begin delete backup AUTO");

                    if (BackupType.FULL.name().equals(deleteBackup.getBackupType())) {
                        final List<DbaasBackup> hidedBackup = getHidedBackup(deleteBackup);

                        childBackups.addAll(hidedBackup);
                        childBackups.add(deleteBackup);
                        deleteBackup(osClient, childBackups, false, projectId);
                    } else if (BackupType.INCREMENTAL.name().equals(deleteBackup.getBackupType())) {
                        final List<DbaasBackup> hidedBackup = getHidedBackup(deleteBackup);

                        final Optional<DbaasBackup> haveHidedParent = hidedBackup.stream().filter(e -> e.getBackupId().equals(deleteBackup.getBackupParent())).findAny();
                        if (haveHidedParent.isPresent()) {
                            childBackups.addAll(hidedBackup);
                            childBackups.add(deleteBackup);
                            deleteBackup(osClient, childBackups, false, projectId);
                        } else {
                            childBackups.add(deleteBackup);
                            deleteBackup(osClient, childBackups, false, projectId);
                        }
                    }


                } else {
                    LOG.info("DBInstance " + instance.getInstanceId() + " still exist!");
                    throw new DBaaSException("Cannot delete AUTO backups when instance exist!");
                }
            }

            commitTransaction();
            log.success();
        } catch (Exception ex) {
            rollbackTransaction();
            LOG.error("Error when delete backup ", ex);
            log.exp(ex).fail();
            if (ex instanceof DBaaSException) {
                throw (DBaaSException) ex;
            }
            throw new DBaaSException("Cannot deleteBackup", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }

    private List<DbaasBackup> getHidedBackup(DbaasBackup deleteBackup) {
        //check hide incremental backup
        final Junction hided = Restrictions.conjunction().add(Restrictions.eq("hided", true))
                .add(Restrictions.eq("deleted", false));
        final List<DbaasBackup> hidedBackup = findCollections(DbaasBackup.class, hided, Order.asc("created"), false);

        final List<String> hideIds = hidedBackup.stream().map(DbaasBackup::getBackupId).collect(Collectors.toList());
        LOG.info("Check hided backup of backups [" + deleteBackup.getBackupId() + "] HideId " + hideIds);
        return hidedBackup;
    }

    @Override
    public void deleteBackup(OSClient osClient, List<DbaasBackup> backups, boolean auto, String projectId) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("deleteBackup");
        try {
            final Date now = new Date();
            final List<String> lsBkIds = backups.stream().map(DbaasBackup::getBackupId).collect(Collectors.toList());
            if(isAnyBackupRestoring(lsBkIds)){
            	throw new Exception("Could not delete backups. There is a restoring process is in-progress");
            }
            for (DbaasBackup bk : backups) {
                LOG.info("Begin delete backups [" + bk.getBackupId() + "]");
//                final ActionResponse res = osClient.trove().backupService().delete(bk.getBackupId());
                
                //call sns_api
                SCClient.SCClientV1 scClientV1 = VsrServiceRef.INSTANCE.scClientService.getSCClient();
                Integer projectIdInt = interfaceProjectService.getByUuid(projectId).getProjectId();
                DeleteBackupRequest deleteBackupRequest = DeleteBackupRequest.builder().backupId(Long.parseLong(bk.getBackupId())).projectId(projectIdInt).build();
                BaseResponse<BackupDbaas> res = scClientV1.trove().backup().delete(deleteBackupRequest);
                
                if (res.getReturnCode() != Integer.toString(HttpStatus.OK.value())) {
                    LOG.error("Error when delete incremental " + bk.getBackupId() + " " + res.getReturnMsg());
                } else {
                    LOG.info("Completed delete backups [" + bk.getBackupId() + "]");
                }

                bk.setDeleted(true);
                bk.setDeletedAt(now);
            }
            for (DbaasBackup backup : backups) {
                update(backup, auto);
            }
            log.success();
        } catch (Exception ex) {
            LOG.error("Error when delete backups", ex);
            log.exp(ex).fail();
            if (ex instanceof DBaaSException) {
                throw (DBaaSException) ex;
            }
            throw new DBaaSException("Cannot delete backups", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }
    
    private boolean isAnyBackupRestoring(List<String> backupIds) throws Exception{
		boolean result = false;
		for(final String bkId : backupIds){
			Boolean isRestoring = false;
			isRestoring = dbaasRedisService.checkBackupIsRestoring(bkId);
			if(isRestoring == true){
				result = true;
			}
		}
		return result;
    }

    @Override
    public void hideBackup(OSClient osClient, List<DbaasBackup> childBackups) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("hideBackup");
        try {
            for (DbaasBackup backup : childBackups) {
                backup.setHided(true);
                update(backup);
            }
            log.success();
        } catch (Exception ex) {
            LOG.error("Error when hide backups", ex);
            log.exp(ex).fail();
            if (ex instanceof DBaaSException) {
                throw (DBaaSException) ex;
            }
            throw new DBaaSException("Cannot hide backups", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }

    @Override
    public void untrashBackup(OSClient osClient, String instanceId) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("untrashBackup");
        try {
            final List<? extends Backup> backupInstance = osClient.trove().backupService().instance(instanceId);
            for (Backup bk : backupInstance) {
                try{
                    beginTransaction();
                    Junction deletedBk = Restrictions.conjunction().add(Restrictions.eq("backupId", bk.getId()))
                            .add(Restrictions.eq("deleted", true));
                    DbaasBackup dbBk = find(DbaasBackup.class, deletedBk, true);
                    if(dbBk!=null){
                        dbBk.setDeleted(false);
                        update(dbBk);
                    }
                }catch(Exception e){
                    rollbackTransaction();
                    throw new DBaaSException("Cannot update status backups", e);
                }
            }
            log.success();
        } catch (Exception ex) {
            LOG.error("Error when untrash backups", ex);
            log.exp(ex).fail();
            if (ex instanceof DBaaSException) {
                throw (DBaaSException) ex;
            }
            throw new DBaaSException("Cannot untrash backups", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }
    
    @Override
    public Integer createSchedulerBackupDaily(OpsUser opsUser, String projectId,
                                              String dbInstanceId, String backupTime,
                                              Integer backupDuration, Integer engineGroup) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("createSchedulerBackupDaily").reqProp("projectId", projectId).reqProp("dbInstanceId", dbInstanceId);
        CLSClient.CLSClientV1 v1 = null;
        try {
            final CLSClient<?> clsClient = ClientThreadLocal.get();
            if (clsClient == null) {
                log.reqProp("v1", "new");
                v1 = ClientFactory.buildV1().endpoint(VsrApiConfig.INSTANCE.sdl_api_url, ServiceType.SCHEDULER_API).onBehaf(opsUser.getUserId())
                        .credential(VsrApiConfig.system_access_key, VsrApiConfig.system_secret_key).build();
            } else {
                log.reqProp("v1", "local");
                v1 = (CLSClient.CLSClientV1) clsClient;
            }


            final UserSchedulerRequest req = prepareSchedulerRequest(opsUser, projectId, dbInstanceId, backupTime, backupDuration, engineGroup);

            final SdlResult result = v1.sdlApi().createUserScheduler(req);
            if (!result.isSuccess()) {
                throw new DBaaSException(result.getErrorMsg());
            }

            log.success();

            return result.getUserSchdId();

        } catch (Exception ex) {
            log.exp(ex).fail();
            throw new DBaaSException("Cannot create backup daily scheduler", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }

    @Override
    public void updateSchedulerBackupDaily(OpsUser opsUser, String projectId, String dbInstanceId, 
                                           String backupTime, Integer backupDuration, 
                                           Integer userSchdId, Integer engineGroup) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("updateSchedulerBackupDaily").reqProp("projectId", projectId).reqProp("dbInstanceId", dbInstanceId);
        CLSClient.CLSClientV1 v1 = null;
        try {
            final CLSClient<?> clsClient = ClientThreadLocal.get();
            if (clsClient == null) {
                log.reqProp("v1", "new");
                v1 = ClientFactory.buildV1().endpoint(VsrApiConfig.INSTANCE.sdl_api_url, ServiceType.SCHEDULER_API).onBehaf(opsUser.getUserId())
                        .credential(VsrApiConfig.system_access_key, VsrApiConfig.system_secret_key).build();
            } else {
                log.reqProp("v1", "local");
                v1 = (CLSClient.CLSClientV1) clsClient;
            }


            final UserSchedulerRequest req = prepareSchedulerRequest(opsUser, projectId, dbInstanceId, backupTime, backupDuration,engineGroup);

            final SdlResult result = v1.sdlApi().updateUserScheduler(userSchdId, req);
            if (!result.isSuccess()) {
                throw new DBaaSException(result.getErrorMsg());
            }
            log.success();
        } catch (Exception ex) {
            log.exp(ex).fail();
            throw new DBaaSException("Cannot create backup daily scheduler", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }

    private UserSchedulerRequest prepareSchedulerRequest(OpsUser opsUser, String projectId, String dbInstanceId, 
                                                         String backupTime, Integer backupDuration, Integer engineGroup) throws JsonProcessingException {
        final LocalTime time = LocalTime.parse(backupTime, DateTimeFormatter.ofPattern("HH:mm"));

        final Map<String, String> param = new LinkedHashMap<>();
        param.put("projectId", projectId);
        param.put("backupDuration", String.valueOf(backupDuration));
        param.put("userId", String.valueOf(opsUser.getUserId()));
        param.put("dbInstanceId", dbInstanceId);
        param.put("engineGroup",String.valueOf(engineGroup));

        final MutableTrigger trigger = CronScheduleBuilder.dailyAtHourAndMinute(time.getHour(), time.getMinute()).build();
        final String cron = ((CronTriggerImpl) trigger).getCronExpression();

        return new UserSchedulerRequestBuilder()
                .userId(opsUser.getUserId())
                .scheduleName(String.format("DB_BK_%s", dbInstanceId))
                .expression(cron)
                .serviceId(Status.ServiceType.DBAAS)
                .backend(VsrApiConfig.INSTANCE.backend)
                .resourceId(dbInstanceId)
                .metricName("BACKUP_DBINSTANCE_SCHEDULER")
                .businessClass("com.iaas.vsr.schedule.job.BackupDBJob")
                .param(new ObjectMapper().writeValueAsString(param))
                .build();
    }

    @Override
    public void disableSchedulerBackupDaily(Integer userId, Integer userSchdId) throws DBaaSException {
        final RootEntity log = RootEntity.create().type("disableSchedulerBackupDaily").reqProp("userSchdId", userSchdId);
        CLSClient.CLSClientV1 v1 = null;
        try {

            final CLSClient<?> clsClient = ClientThreadLocal.get();
            if (clsClient == null) {
                log.reqProp("v1", "new");
                v1 = ClientFactory.buildV1().endpoint(VsrApiConfig.INSTANCE.sdl_api_url, ServiceType.SCHEDULER_API).onBehaf(userId)
                        .credential(VsrApiConfig.system_access_key, VsrApiConfig.system_secret_key).build();
            } else {
                log.reqProp("v1", "local");
                v1 = (CLSClient.CLSClientV1) clsClient;
            }


            final SdlResult result = v1.sdlApi().disableUserScheduler(userSchdId);

            if (!result.isSuccess()) {
                throw new DBaaSException(result.getErrorMsg());
            }
            log.success();
        } catch (SchedulerException ex) {
            log.exp(ex).fail();
            throw new DBaaSException("Cannot disable daily scheduler", ex);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(log);
        }
    }

    @Override
    public String genBackupName(String dbInstanceId, Integer engineGroup) {
        SimpleDateFormat pattern = new SimpleDateFormat("yyyyMMddHHmm");
        final String dateStr = pattern.format(new Date());
        return String.format("%s_%s_%s",DbaasConstant.BackupPrefix.get(engineGroup), dbInstanceId.replaceAll("-", "_"), dateStr);
    }
    
    @Override
    public String genBackupName(String dbInstanceId, String postFix, Integer engineGroup) {
        return String.join("_", this.genBackupName(dbInstanceId,engineGroup), postFix);
    }

}
