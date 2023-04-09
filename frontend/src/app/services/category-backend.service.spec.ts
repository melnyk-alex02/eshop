import { TestBed } from '@angular/core/testing';

import { CategoryBackendService } from './category-backend.service';

describe('CategoryBackendService', () => {
  let service: CategoryBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoryBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
