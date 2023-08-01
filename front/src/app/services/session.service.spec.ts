import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let sessionService: SessionService;
  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };

  beforeEach(() => {
    sessionService = new SessionService();
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(sessionService.isLogged).toBe(false);
    expect(sessionService.sessionInformation).toBeUndefined();
  });

  it('should emit isLogged changes', (done) => {
    sessionService.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });

    sessionService.logIn(mockSessionInformation);
  });

  it('should log in a user', () => {
    sessionService.logIn(mockSessionInformation);

    expect(sessionService.isLogged).toBe(true);
    expect(sessionService.sessionInformation).toBe(mockSessionInformation);
  });

  it('should log out a user', () => {
    sessionService.logIn(mockSessionInformation);
    sessionService.logOut();

    expect(sessionService.isLogged).toBe(false);
    expect(sessionService.sessionInformation).toBeUndefined();
  });
});
