export interface User {
  userId: string;
  email: string;
  emailVerified: boolean;
  firstName: string;
  lastName: string;
  registeredDate: Date;
  roles: string[];
}
