import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  // Admin user with sessionInformation.admin set to true
  const mockAdminSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  // Non-admin user with sessionInformation.admin set to false
  const mockNonAdminSessionService = {
    sessionInformation: {
      admin: false,
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
    }).compileComponents();
  });

  // Test the display of "Create" button for an admin user
  it('should display "Create" button for an admin user', () => {
    TestBed.overrideProvider(SessionService, { useValue: mockAdminSessionService });
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeTruthy();
  });

  // Test the absence of "Create" button for a non-admin user
  it('should not display "Create" button for a non-admin user', () => {
    TestBed.overrideProvider(SessionService, { useValue: mockNonAdminSessionService });
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeFalsy();
  });

});
