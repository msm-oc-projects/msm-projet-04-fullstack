import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Teacher } from '../models/teacher.interface';
import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTesting: HttpTestingController;
  const teacher: Teacher = {
    id: 1,
    firstName: 'First',
    lastName: 'Last',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(TeacherService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('gets all teachers', () => {
    service.all().subscribe(value => expect(value).toEqual([teacher]));
    const request = httpTesting.expectOne('api/teacher');
    expect(request.request.method).toBe('GET');
    request.flush([teacher]);
  });

  it('gets one teacher', () => {
    service.detail('1').subscribe(value => expect(value).toEqual(teacher));
    const request = httpTesting.expectOne('api/teacher/1');
    expect(request.request.method).toBe('GET');
    request.flush(teacher);
  });
});
