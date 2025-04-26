export interface IUser {
  userId: number;
  full_name: string;
  email: string;
  password: string;
  userType: "USER" | "OWNER" | "UNAUTHENTICATED";
}
