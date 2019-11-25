import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordoverviewComponent } from './passwordoverview.component';

describe('PasswordoverviewComponent', () => {
  let component: PasswordoverviewComponent;
  let fixture: ComponentFixture<PasswordoverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PasswordoverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordoverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
