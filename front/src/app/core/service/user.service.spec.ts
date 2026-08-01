import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { User } from '../models/user.interface';
import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpTesting: HttpTestingController;
  const user: User = {
    id: 1,
    email: 'user@example.com',
    firstName: 'First',
    lastName: 'Last',
    admin: false,
    password: '',
    createdAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(UserService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('gets a user', () => {
    service.getById('1').subscribe(value => expect(value).toEqual(user));
    const request = httpTesting.expectOne('api/user/1');
    expect(request.request.method).toBe('GET');
    request.flush(user);
  });

  it('deletes a user', () => {
    service.delete('1').subscribe(value => expect(value).toBeNull());
    const request = httpTesting.expectOne('api/user/1');
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });
});
