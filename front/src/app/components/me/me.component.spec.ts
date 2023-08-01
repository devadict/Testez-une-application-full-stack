import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";
import {Router} from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userServiceMock: jest.Mocked<UserService>
  let routerServiceMock: jest.Mocked<Router>;
  let matSnackBarServiceMock: jest.Mocked<MatSnackBar>;
  let sessionServiceMock: jest.Mocked<SessionService>;

  const mockUser = {
    admin: true,
    id: 1
  } as User;

  beforeEach(async () => {
    userServiceMock = {getById: jest.fn(), delete: jest.fn()} as unknown as jest.Mocked<UserService>;
    sessionServiceMock = {logOut: jest.fn(), sessionInformation: mockUser} as unknown as jest.Mocked<SessionService>;
    routerServiceMock = {navigate: jest.fn()} as unknown as jest.Mocked<Router>;
    matSnackBarServiceMock = {open: jest.fn()} as unknown as jest.Mocked<MatSnackBar>;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: UserService, useValue: userServiceMock},
        { provide: Router, useValue: routerServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarServiceMock }

      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('Should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('Should get the data of the user using userid', () => {
    userServiceMock.getById.mockReturnValue(of(mockUser));
    fixture.detectChanges()
    expect(component.user).toEqual(mockUser)
  })

  it('Should delete the account and redirect to the home page', () => {
    userServiceMock.delete.mockReturnValue(of(null));

    component.delete();
    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarServiceMock.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 })
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerServiceMock.navigate).toHaveBeenCalledWith(["/"])
  })



  it('Should call window.history.back on back()', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(spy).toHaveBeenCalled();
  })
});
