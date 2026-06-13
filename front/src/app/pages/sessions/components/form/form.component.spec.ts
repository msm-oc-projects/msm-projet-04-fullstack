import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../../../core/models/session.interface';
import { SessionApiService } from '../../../../core/service/session-api.service';
import { SessionService } from '../../../../core/service/session.service';
import { TeacherService } from '../../../../core/service/teacher.service';
import { FormComponent } from './form.component';

describe('FormComponent integration', () => {
  let fixture: ComponentFixture<FormComponent>;
  let component: FormComponent;
  const existingSession: Session = {
    id: 10,
    name: 'Morning yoga',
    date: new Date('2026-06-13'),
    description: 'Session',
    teacher_id: 2,
    users: [1]
  };
  const sessionApiService = {
    detail: jest.fn(() => of(existingSession)),
    create: jest.fn((session: Session) => of(session)),
    update: jest.fn((_id: string, session: Session) => of(session))
  };
  const teacherService = { all: jest.fn(() => of([])) };
  const sessionService = { sessionInformation: { admin: true } };
  const router = { url: '/sessions/create', navigate: jest.fn() };
  const snackBar = { open: jest.fn() };

  beforeEach(async () => {
    jest.clearAllMocks();
    router.url = '/sessions/create';
    sessionService.sessionInformation.admin = true;
    TestBed.overrideProvider(MatSnackBar, { useValue: snackBar });
    await TestBed.configureTestingModule({
      imports: [FormComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '10' } } } },
        { provide: SessionApiService, useValue: sessionApiService },
        { provide: TeacherService, useValue: teacherService },
        { provide: SessionService, useValue: sessionService },
        { provide: Router, useValue: router },
        { provide: MatSnackBar, useValue: snackBar }
      ]
    }).compileComponents();
  });

  function createComponent(): void {
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('initializes an empty creation form', () => {
    createComponent();
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: ''
    });
  });

  it('redirects non-admin users', () => {
    sessionService.sessionInformation.admin = false;
    createComponent();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('creates a session and returns to the list', () => {
    createComponent();
    component.sessionForm?.setValue({
      name: 'New session',
      date: '2026-06-14',
      teacher_id: 2,
      description: 'Description'
    });

    component.submit();

    expect(sessionApiService.create).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('loads and updates an existing session', () => {
    router.url = '/sessions/update/10';
    createComponent();

    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value.name).toBe('Morning yoga');

    component.submit();

    expect(sessionApiService.update).toHaveBeenCalledWith('10', expect.any(Object));
    expect(snackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
  });
});
