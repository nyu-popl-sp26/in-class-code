// Depth subtyping for const fields allows us to conclude that 
// tau_1 = {const f: Num, const g: Num} <: tau_2 = {const f: Num}
// Using SubObjDepth and SubAny
const f = function(x) { // x: tau_2
    return x.f + 1 
}
const o = {f: 3, g: 4} 
f(o) 
// Well-typed, using TypeCall + TypeSub 

// In contrast, there is no depth subtyping for let fields 
// tau_3 = {let h: tau_1} <: tau_4 = {let h: tau_2}
// We know that tau_1 <: tau_2 
// Suppose we had SubObjDepth' allowing us to conclude tau_3 <: tau_4 
const o1 = {h: o} 
o1.h = {f:100} // Well-typed, using SubObjDepth' 
// o1.h is of type tau_1, {f:100} is of type tau_2 
console.log(o1.h.g) // Well-typed, using TypeDerefFld + TypeSub 
// But gets stuck at runtime, breaking type soundness 