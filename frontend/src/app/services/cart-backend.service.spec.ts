import { TestBed } from '@angular/core/testing';

import { CartBackendService } from './cart-backend.service';

describe('CartBackendService', () => {
  let service: CartBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CartBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
