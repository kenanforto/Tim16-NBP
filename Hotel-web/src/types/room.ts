export class Room {
  id!: number;
  type!: { id: number; description: string };
  status!: { id: number; description: string };
  description?: string;
  floor!: number;
  price!: number;
  image!: string;
}
