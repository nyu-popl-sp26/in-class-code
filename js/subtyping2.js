// TypeCall 
const f1 = function(o) { // function(o: {const x: Num, const y: Num})
    o.x = 1
    o.y = 2 
    return o 
}

const o1 = {x: 4, y: 3, z: 5} // {const x: Num, const y: Num, const z: Num}
console.log(f1(o1))

// TypeIf 
const o = true ? {f: 0, g: true} : {f: 2, h: 0}
// {let f: Num} <: Any 
console.log(2 * o.f)