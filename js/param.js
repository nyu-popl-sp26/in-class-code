// Sequencing 
let x = 1; 
x = x + 1; 
x = x + 1; 
console.log(x)

// Encoding sequencing using call-by-value functions 
let x1 = 1; console.log((() => x1 = x1 + 1)(x1 = x1 + 1))

// While loops 
let y = 10; 
while (y >= 0) {
    console.log(y);
    y = y - 1; 
}

// Encoding while loops using call-by-name functions 
function my_while(cond) {
    return body => cond() ? (body(), my_while(cond)(body)) : 2
}   

let y1 = 10; 
my_while(() => y1 >= 0)(() => (console.log(y1), y1 = y1 - 1))

// Evaluate argument y1 >= 0 == true 
// body => true ? (body, my_while(true)(body)) : 2 
// Evaluate argument console.log(y1), y1 = y1 - 1 == 9 
// true ? (9, my_while(true)(9)) : 2 
// Evaluate my_while(true)(9)
// body => true ? (body, my_while(true)(body)) : 2 
// true ? (9, my_while(true)(9)) : 2 
// ... 

// Encoding for-loops using while loops 
for (let x = 0; x <= 10; x++) {
    console.log(x);
}

function my_for(cond) {
    return body => cond() ? (body(), my_for(cond)(body)) : 2
}

let z = 100; 
// my_for(() => z <= 110)(() => (console.log(z), z = z + 1))
my_while(() => z <= 110)(() => (console.log(z), z = z + 1))

// Repeat/until loops 
// let u = 0; 
// repeat u = u + 1 until u > 100 

function repeat(cond) {
    return body => !cond() ? (body(), repeat(cond)(body)) : 2
}

// Populate an array with 3 random numbers > 0.3 
let u = []; 
repeat(() => u.length > 3)(() => { console.log(u.length); const n = Math.random(); return (n > 0.3 ? u.push(n) : u)})

// Print the contents of the array 
let i = 0; 
my_for(() => i < 3)(() => (console.log(u[i]), i = i + 1))

