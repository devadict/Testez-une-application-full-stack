import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let sessionApiService: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });

    sessionApiService = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(sessionApiService).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [] },
      { id: 2, name: 'Session 2', description: 'Description 2', date: new Date(), teacher_id: 2, users: [] }
    ];

    sessionApiService.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should fetch session details', () => {
    const sessionId = '1';
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: []
    };

    sessionApiService.detail(sessionId).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session', () => {
    const sessionId = '1';

    sessionApiService.delete(sessionId).subscribe();

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a session', () => {
    const session: Session = {
      name: 'New Session',
      description: 'New Session Description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };

    sessionApiService.create(session).subscribe((createdSession) => {
      expect(createdSession).toEqual(session);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(session);
    req.flush(session);
  });

  it('should update a session', () => {
    const sessionId = '1';
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Session',
      description: 'Updated Session Description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };

    sessionApiService.update(sessionId, updatedSession).subscribe((updatedSession) => {
      expect(updatedSession).toEqual(updatedSession);
    });

    const req = httpTestingController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should participate in a session', () => {
    const sessionId = '1';
    const userId = '1';

    sessionApiService.participate(sessionId, userId).subscribe();

    const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should unparticipate in a session', () => {
    const sessionId = '1';
    const userId = '1';

    sessionApiService.unParticipate(sessionId, userId).subscribe();

    const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
