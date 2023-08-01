import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
 const mockSessionInformation: SessionInformation = {
 token: '',
 type: '',
 id: 1,
 username: '',
 firstName: '',
 lastName: '',
 admin: true,
 };

 let component: LoginComponent;
 let fixture: ComponentFixture<LoginComponent>;
 let mockRouter: Router;
 let mockSessionService: jest.Mocked<SessionService>;
 let mockAuthService: jest.Mocked<AuthService>;

 beforeEach(async () => {
 mockRouter = {
 navigate: jest.fn(),
 } as unknown as jest.Mocked<Router>;

 mockAuthService = {
 login: jest.fn().mockReturnValue(of(undefined)),
 } as unknown as jest.Mocked<AuthService>;

 mockSessionService = {
 logIn: jest.fn().mockReturnValue(of(mockSessionInformation)),
 } as unknown as jest.Mocked<SessionService>;

 await TestBed.configureTestingModule({
 declarations: [LoginComponent],
 providers: [
 { provide: SessionService, useValue: mockSessionService },
 { provide: AuthService, useValue: mockAuthService },
 { provide: Router, useValue: mockRouter },
 ],
 imports: [
 RouterTestingModule,
 BrowserAnimationsModule,
 HttpClientModule,
 MatCardModule,
 MatIconModule,
 MatFormFieldModule,
 MatInputModule,
 ReactiveFormsModule,
 ],
 }).compileComponents();

 fixture = TestBed.createComponent(LoginComponent);
 component = fixture.componentInstance;
 fixture.detectChanges();

 mockRouter = TestBed.inject(Router);
 });

 it('should create', () => {
  expect(component).toBeTruthy();
});

it('should initialize the form with empty email and password fields', () => {
  const emailControl = component.form.get('email');
  const passwordControl = component.form.get('password');

  expect(emailControl?.value).toEqual('');
  expect(passwordControl?.value).toEqual('');
});

it('should disable the submit button when the form is invalid', () => {
  const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
  const emailControl = component.form.get('email');
  const passwordControl = component.form.get('password');

  emailControl?.setValue('test@example.com');
  passwordControl?.setValue('password');
  fixture.detectChanges();

  expect(submitButton.disabled).toBeFalsy();

  emailControl?.setValue('');
  fixture.detectChanges();

  expect(submitButton.disabled).toBeTruthy();
});

it('should toggle password visibility when clicking the visibility toggle button', () => {
  const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');
  const visibilityToggleButton = fixture.nativeElement.querySelector('button[matSuffix]');

  expect(passwordInput.type).toEqual('password');

  visibilityToggleButton.click();
  fixture.detectChanges();

  expect(passwordInput.type).toEqual('text');

  visibilityToggleButton.click();
  fixture.detectChanges();

  expect(passwordInput.type).toEqual('password');
});

});
