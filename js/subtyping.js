// function reset1(o: {const x: Num, const y: Num})
const reset1 = function(o) {
    return o.x + o.y 
}

// function reset2(o: {const x: {const whole: Num}})
const reset2 = function(o) {
    return o.x.whole + 1 
}

const o1 = {x: 3, y: 4} 
const o2 = {y: 4, x: 3, z: 5} // {const y: Num, const x: Num}
const o3 = {x: {whole: 3, decimal: 14}, y: 4}

// Which combinations are typesafe? 