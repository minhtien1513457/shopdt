import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  private readonly TICKET = 'ticket';
  private readonly LANG = 'lang';
  private readonly EXPIRED = 'expired';
  private readonly ROLE = 'role';

  /**Save ticket */
  saveTicket(data) {
    window.localStorage.setItem(this.TICKET, JSON.stringify(data));
  }

  /**Save role */
  saveRole(data) {
    window.localStorage.setItem(this.ROLE, data);
  }

  /**Save expired */
  saveExpired(data) {

    window.localStorage.setItem(this.EXPIRED, JSON.stringify(data));
  }

  /**Save language */
  saveLanguage(data) {
    window.localStorage.setItem(this.LANG, data);
  }

  /**Get expired  */
  getExpired() {
    if (!window.localStorage.getItem(this.EXPIRED)) {
      return 0;
    }
    return window.localStorage.getItem(this.EXPIRED);
  }

  /**Get language */
  getLanguage() {
    if (!window.localStorage.getItem(this.LANG)) {
      return "en";
    }
    return window.localStorage.getItem(this.LANG);
  }

  /**Get role */
  getRole() {
    if (!window.localStorage.getItem(this.ROLE)) {
      return null;
    }
    return window.localStorage.getItem(this.ROLE);
  }

  /**Get ticket */
  getTicket() {
    if (!window.localStorage.getItem(this.TICKET)) {
      return null;
    }
    const ticketObj = JSON.parse(window.localStorage.getItem(this.TICKET));
    return {
      ticket: ticketObj.ticket,
      userName: ticketObj.userName
    };
  }

  /**Clear local storage */
  clearStorage() {
    window.localStorage.removeItem(this.TICKET);
    window.localStorage.removeItem(this.EXPIRED)
  }
}

