import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RendimentosTableComponent } from './rendimentos-table.component';

describe('RendimentosTableComponent', () => {
  let component: RendimentosTableComponent;
  let fixture: ComponentFixture<RendimentosTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RendimentosTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RendimentosTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
