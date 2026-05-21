import { Product, Customer } from './types';

export const ALL_PRODUCTS: Product[] = [
  { id: 'p1', name: 'Fresh Bakery Bread', weight: 2, value: 20 },
  { id: 'p2', name: 'Magic Potion', weight: 4, value: 60 },
  { id: 'p3', name: 'Spell Book', weight: 5, value: 80 },
  { id: 'p4', name: "Jiji's Treats", weight: 1, value: 10 },
  { id: 'p5', name: 'Crystal Ball', weight: 6, value: 100 },
  { id: 'p6', name: 'Spare Broom', weight: 3, value: 50 },
  { id: 'p7', name: 'Witch Hat', weight: 2, value: 40 },
  { id: 'p8', name: 'Cauldron', weight: 8, value: 120 },
];

export const LOCATIONS: Record<string, { x: number, y: number }> = {
  'Koriko City': { x: 75, y: 65 }, // Kiki's home base
  'Emishi Village': { x: 55, y: 15 },
  'Kingsbury': { x: 45, y: 10 },
  "Howl's Moving Castle": { x: 65, y: 35 },
  'Laputa': { x: 15, y: 15 },
  'Market Chipping': { x: 40, y: 38 },
  'Bathhouse': { x: 85, y: 25 },
  "Fujimoto's Boat": { x: 50, y: 85 },
};

export const INITIAL_CUSTOMERS: Customer[] = [
  { id: 'c1', name: 'Abc', location: 'Emishi Village', quantity: 1, productName: 'Magic Potion', weight: 4, value: 60 },
  { id: 'c2', name: 'Ashitaka', location: 'Kingsbury', quantity: 1, productName: 'Spare Broom', weight: 3, value: 50 },
  { id: 'c3', name: 'Howl', location: "Howl's Moving Castle", quantity: 1, productName: 'Spell Book', weight: 5, value: 80 },
  { id: 'c4', name: 'King', location: 'Laputa', quantity: 1, productName: 'Crystal Ball', weight: 6, value: 100 },
  { id: 'c5', name: 'Sophie', location: 'Market Chipping', quantity: 1, productName: 'Witch Hat', weight: 2, value: 40 },
  { id: 'c6', name: 'Chihiro', location: 'Bathhouse', quantity: 1, productName: 'Fresh Bakery Bread', weight: 2, value: 20 },
  { id: 'c7', name: 'Ponyo', location: "Fujimoto's Boat", quantity: 1, productName: "Jiji's Treats", weight: 1, value: 10 },
];
