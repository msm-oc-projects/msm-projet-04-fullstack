import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Session } from '../models/session.interface';
import { SessionApiService } from './session-api.service';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTesting: HttpTestingController;
  const session: Session = {
    id: 1,
    name: 'Yoga',
    description: 'Session',
    date: new Date('2026-06-13'),
    teacher_id: 2,
    users: []
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(SessionApiService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('gets all sessions', () => {
    service.all().subscribe(value => expect(value).toEqual([session]));
    const request = httpTesting.expectOne('api/session');
    expect(request.request.method).toBe('GET');
    request.flush([session]);
  });

  it('gets one session', () => {
    service.detail('1').subscribe(value => expect(value).toEqual(session));
    const request = httpTesting.expectOne('api/session/1');
    expect(request.request.method).toBe('GET');
    request.flush(session);
  });

  it('deletes a session', () => {
    service.delete('1').subscribe(value => expect(value).toBeNull());
    const request = httpTesting.expectOne('api/session/1');
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });

  it('creates a session', () => {
    service.create(session).subscribe(value => expect(value).toEqual(session));
    const request = httpTesting.expectOne('api/session');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(session);
    request.flush(session);
  });

  it('updates a session', () => {
    service.update('1', session).subscribe(value => expect(value).toEqual(session));
    const request = httpTesting.expectOne('api/session/1');
    expect(request.request.method).toBe('PUT');
    request.flush(session);
  });

  it('participates in a session', () => {
    service.participate('1', '2').subscribe(value => expect(value).toBeNull());
    const request = httpTesting.expectOne('api/session/1/participate/2');
    expect(request.request.method).toBe('POST');
    request.flush(null);
  });

  it('stops participating in a session', () => {
    service.unParticipate('1', '2').subscribe(value => expect(value).toBeNull());
    const request = httpTesting.expectOne('api/session/1/participate/2');
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });
});
