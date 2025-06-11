export interface AddressRequest {
  street: string;
  city: string;
  country: string;
  zipCode: string;
}

export interface RegistrationRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  birthDate: string;
  addressId: number;
}

export interface AuthenticationRequest {
  username: string;
  password: string;
}

export interface BookingRequest {
  guestId: number;
  reservationAgentId: number;
  dateFrom: string;
  dateTo: string;
  bookingStatusId: number;
}

export interface GuestRequest {
  firstName: string;
  lastName: string;
  email: string;
  addressId: number;
  phoneNumber: string;
  birthDate: string;
}

export interface PaymentRequest {
  bookingId: number;
  date: string;
  payment: number;
  paymentTypeId: number;
  paymentStatusId: number;
}

export interface RoomRequest {
  roomTypeId: number;
  roomStatusId: number;
  floor: number;
  price: number;
  description: string;
}

export interface RoomTypeRequest {
  description: string;
}

export interface RoleRequest {
  name: string;
}

export interface RoomStatusRequest {
  description: string;
}

export interface PaymentTypeRequest {
  paymentType: string;
}

export interface RoomBookedRequest {
  bookingId: number;
  roomId: number;
}

export interface PaymentStatusRequest {
  status: string;
  description: string;
}

export interface BookingStatusRequest {
  status: string;
  description: string;
  active: number;
}

export interface Room {
  id: number;
  name: string;
  type: string;
  status: string;
  floor: number;
  price: number;
  description: string;
}

export interface Image {
  id: number;
  name: string;
  type: string;
  imageData: string;
  roomId: number;
}
