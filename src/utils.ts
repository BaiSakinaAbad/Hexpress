import { Product, Customer } from './types';

/**
 * Solves the 0/1 Knapsack problem.
 * @param items Array of available products
 * @param maxCapacity The maximum weight Kiki's basket can carry
 * @returns The optimal list of products to maximize value within the weight limit
 */
export function solveKnapsack(items: Product[], maxCapacity: number): Product[] {
  const n = items.length;
  // dp[i][w] will store the maximum value achievable with the first 'i' items and capacity 'w'
  const dp: number[][] = Array(n + 1).fill(0).map(() => Array(maxCapacity + 1).fill(0));

  for (let i = 1; i <= n; i++) {
    for (let w = 0; w <= maxCapacity; w++) {
      const currentItem = items[i - 1];
      if (currentItem.weight <= w) {
        // Condition: we can either include the item or exclude it. We take the max of both.
        dp[i][w] = Math.max(
          dp[i - 1][w], // exclude
          dp[i - 1][w - currentItem.weight] + currentItem.value // include
        );
      } else {
        // Item is too heavy, skip it
        dp[i][w] = dp[i - 1][w];
      }
    }
  }

  // Backtrack to find which items were selected to get the optimal value
  let remainingCapacity = maxCapacity;
  let remainingValue = dp[n][maxCapacity];
  const selectedItems: Product[] = [];

  for (let i = n; i > 0 && remainingValue > 0; i--) {
    // If the value came from the row above without including this item
    if (remainingValue === dp[i - 1][remainingCapacity]) {
      continue; // This item was not included
    } else {
      // This item was included
      const item = items[i - 1];
      selectedItems.push(item);
      remainingValue -= item.value;
      remainingCapacity -= item.weight;
    }
  }

  return selectedItems; // Return the selected items
}

export function solveTSP(locations: string[], coords: Record<string, {x: number, y: number}>): string[] {
  const startAndEnd = 'Koriko City';
  const placesToVisit = Array.from(new Set(locations.filter(loc => loc !== startAndEnd)));

  if (placesToVisit.length === 0) return [startAndEnd, startAndEnd];

  const permutations: string[][] = [];
  function permute(arr: string[], m: string[] = []) {
    if (arr.length === 0) {
      permutations.push(m);
    } else {
      for (let i = 0; i < arr.length; i++) {
        const curr = arr.slice();
        const next = curr.splice(i, 1);
        permute(curr.slice(), m.concat(next));
      }
    }
  }
  permute(placesToVisit);

  const getDistance = (l1: string, l2: string) => {
    const p1 = coords[l1] || { x: 50, y: 50 };
    const p2 = coords[l2] || { x: 50, y: 50 };
    return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
  };

  let minDistance = Infinity;
  let bestRoute: string[] = [];

  for (const perm of permutations) {
    let dist = 0;
    let current = startAndEnd;
    for (const stop of perm) {
      dist += getDistance(current, stop);
      current = stop;
    }
    dist += getDistance(current, startAndEnd);

    if (dist < minDistance) {
      minDistance = dist;
      bestRoute = [startAndEnd, ...perm, startAndEnd];
    }
  }

  return bestRoute;
}

/**
 * Naive String Matching algorithm for search.
 */
export function naiveStringMatch(text: string, pattern: string): boolean {
  if (!pattern) return true;
  text = text.toLowerCase();
  pattern = pattern.toLowerCase();
  const n = text.length;
  const m = pattern.length;
  for (let i = 0; i <= n - m; i++) {
    let j = 0;
    while (j < m && text[i + j] === pattern[j]) {
      j++;
    }
    if (j === m) return true;
  }
  return false;
}

/**
 * Merge Sort algorithm to sort the customer list.
 */
export function mergeSortCustomers(arr: Customer[], sortBy: 'name' | 'value' | 'weight'): Customer[] {
  if (arr.length <= 1) return arr;

  const mid = Math.floor(arr.length / 2);
  const left = mergeSortCustomers(arr.slice(0, mid), sortBy);
  const right = mergeSortCustomers(arr.slice(mid), sortBy);

  return merge(left, right, sortBy);
}

function merge(left: Customer[], right: Customer[], sortBy: 'name' | 'value' | 'weight'): Customer[] {
  let result: Customer[] = [];
  let i = 0, j = 0;

  while (i < left.length && j < right.length) {
    let condition = false;
    if (sortBy === 'name') {
      condition = left[i].name.localeCompare(right[j].name) <= 0;
    } else if (sortBy === 'value') {
      condition = left[i].value >= right[j].value; // Highest to lowest
    } else {
      condition = left[i].weight <= right[j].weight; // Lowest to highest
    }

    if (condition) {
      result.push(left[i++]);
    } else {
      result.push(right[j++]);
    }
  }

  return result.concat(left.slice(i)).concat(right.slice(j));
}

