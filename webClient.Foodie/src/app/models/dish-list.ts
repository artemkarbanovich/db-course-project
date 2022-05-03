import { Photo } from "./photo";

export interface DishList {
    id: number,
    name: string,
    price: number,
    isVisible: boolean,
    photos: Photo[]
}
