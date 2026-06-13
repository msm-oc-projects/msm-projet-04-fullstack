import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../../../core/models/session.interface';
import { Teacher } from '../../../../core/models/teacher.interface';
import { SessionApiService } from '../../../../core/service/session-api.service';
import { SessionService } from '../../../../core/service/session.service';
import { TeacherService } from '../../../../core/service/teacher.service';
import { DetailComponent } from './detail.component';

describe('DetailComponent integration', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  const session: Session = {
    id: 10,
    name: 'Morning yoga',
    date: new Date('2026-06-13'),
    description: 'Session',
    teacher_id: 2,
    users: []
  };
  const teacher: Teacher = {
    id: 2,
    firstName: 'First',
    lastName: 'Teacher',
    createdAt: new Date(),
    updatedAt: new Date()
  };
  const sessionApiService = {
    detail: jest.fn(() => of(session)),
    delete: jest.fn(() => of(undefined)),
    participate: jest.fn(() => of(undefined)),
    unParticipate: jest.fn(() => of(undefined))
  };
  const teacherService = { detail: jest.fn(() => of(teacher)) };
  const sessionService = { sessionInformation: { id: 1, admin: false } };
  const router = { navigate: jest.fn() };
  const snackBar = { open: jest.fn() };

  beforeEach(async () => {
    jest.clearAllMocks();
    session.users = [];
    sessionService.sessionInformation.admin = false;
    TestBed.overrideProvider(MatSnackBar, { useValue: snackBar });
    await TestBed.configureTestingModule({
      imports: [DetailComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '10' } } } },
        { provide: SessionApiService, useValue: sessionApiService },
        { provide: TeacherService, useValue: teacherService },
        { provide: SessionService, useValue: sessionService },
        { provide: Router, useValue: router },
        { provide: MatSnackBar, useValue: snackBar }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('loads the session and its teacher', () => {
    expect(component.session).toEqual(session);
    expect(component.teacher).toEqual(teacher);
    expect(component.isParticipate).toBe(false);
    expect(fixture.nativeElement.textContent).toContain('Morning Yoga');
  });

  it('detects an existing participation', () => {
    session.users = [1];
    component.ngOnInit();
    expect(component.isParticipate).toBe(true);
  });

  it('participates and refreshes the session', () => {
    component.participate();
    expect(sessionApiService.participate).toHaveBeenCalledWith('10', '1');
    expect(sessionApiService.detail).toHaveBeenCalledTimes(2);
  });

  it('stops participating and refreshes the session', () => {
    component.unParticipate();
    expect(sessionApiService.unParticipate).toHaveBeenCalledWith('10', '1');
    expect(sessionApiService.detail).toHaveBeenCalledTimes(2);
  });

  it('deletes the session and navigates to the list', () => {
    component.delete();
    expect(sessionApiService.delete).toHaveBeenCalledWith('10');
    expect(snackBar.open).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('goes back in browser history', () => {
    const back = jest.spyOn(window.history, 'back').mockImplementation();
    component.back();
    expect(back).toHaveBeenCalled();
    back.mockRestore();
  });
});
