let x = 1; 
x = x + 1; 
console.log(x = x + 1);
console.log(x)

let x1 = 2; 
let y1 = 2; 
y1 = x1 = 3; 
console.log(x1+y1) 

// JavaScript treats mutable variable o1 as a value 
let o1 = 42; 
let o2 = o1; 
o1 = o1 + 2; 
console.log(o1);
console.log(o2)

// Whereas objects are treated as references 
let obj1 = { x: 42 };
let obj2 = obj1; 
obj2.x = obj1.x + 2; 
console.log(obj1.x); 
console.log(obj2.x)

// New behaviors for our typechecker to catch 
const y = 3; 
y = y + 1

let z = 3; 
z = true