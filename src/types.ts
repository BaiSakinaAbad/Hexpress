export type Screen = 'intro1' | 'intro2' | 'knapsack' | 'search' | 'tsp' | 'exit';

export interface Product {
  id: string;
  name: string;
  weight: number;
  value: number;
}

export interface Customer {
  id: string;
  name: string;
  location: string;
  quantity: number;
  productName: string;
  weight: number;
  value: number;
}
