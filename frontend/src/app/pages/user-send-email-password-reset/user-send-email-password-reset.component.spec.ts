import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSendEmailPasswordResetComponent } from './user-send-email-password-reset.component';

describe('UserSendEmailPasswordResetComponent', () => {
  let component: UserSendEmailPasswordResetComponent;
  let fixture: ComponentFixture<UserSendEmailPasswordResetComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserSendEmailPasswordResetComponent]
    });
    fixture = TestBed.createComponent(UserSendEmailPasswordResetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
