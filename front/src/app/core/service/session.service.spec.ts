import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../models/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('publishes login and logout state', () => {
    const states: boolean[] = [];
    const subscription = service.$isLogged().subscribe(value => states.push(value));
    const session: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'user@example.com',
      firstName: 'First',
      lastName: 'Last',
      admin: false
    };

    service.logIn(session);
    expect(service.sessionInformation).toEqual(session);
    expect(service.isLogged).toBe(true);

    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);
    expect(states).toEqual([false, true, false]);
    subscription.unsubscribe();
  });
});
