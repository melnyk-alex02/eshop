export interface User {
  userId: string;
  email: string;
  emailVerified: boolean;
  firstName: string;
  lastName: string;
  registerDate: Date;
  roles: string[];
}
