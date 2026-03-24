// Lambda calculus (Church, 1936)
// e ::= x | e1(e2) | x => e

// Church Booleans
const ctrue = x => y => x

const cfalse = x => y => y

const toBool = b => b(true)(false)

// Subtlety due to call-by-value evaluation: 
// b ? e1 : e2 is translated into ite(b)(() => e1)(() => e2)
// e1 and e2 are wrapped in functions to ensure that only the selected branch is evaluated.
const ite = b => e1 => e2 => b(e1)(e2)() 

const and = b1 => b2 => b1(b2)(cfalse)

const or = b1 => b2 => b1(ctrue)(b2)

const not = b => b(cfalse)(ctrue)

// Church pairs
const pair = n => m => (b => b(n)(m))

const fst = p => p(ctrue)

const snd = p => p(cfalse)

// Church numerals
const zero = s => z => z 

const one = s => z => s(z)

const two = s => z => s(s(z))

const toInt = n => n(x => x + 1)(0) 

const plusOne = n => s => z => s (n(s)(z))

// const plus = n => m => (s => z => m(s)(n(s)(z)))
 
const plus = n => m => m(plusOne)(n)

function fromInt(n) {
    return n === 0 ? zero : plus(one)(fromInt(n-1));
}

const mult = n => m => m(plus(n))(zero)

const isZero = n => n(x => cfalse)(ctrue)

const minusOne = n => fst(n(p => pair(snd(p))(plus(snd(p))(one)))(pair(zero)(zero)))

// Recursion 
const facFun = fac => n =>
      ite(isZero(n))(() => one)(() => mult(n)(fac(minusOne(n))))

const fac0 = facFun(n => ctwo) // fac0 can be any function 
const fac1 = facFun(fac0)
const fac2 = facFun(fac1)

// Z combinator for call-by-value 
const fix = f => (x => f(y => x(x)(y)))(x => f(y => x(x)(y)))

// Y combinator for call-by-name (no beta-reductions inside abstractions)
const fix_y = f => (x => f(x(x)))(x => f(x(x)))

const fac = fix(facFun)

const result = toInt(fac(fromInt(5)))
      
console.log(result)