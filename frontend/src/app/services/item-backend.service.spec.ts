import { TestBed } from '@angular/core/testing';

import { ItemBackendService } from './item-backend.service';

describe('ItemBackendService', () => {
  let service: ItemBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItemBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
