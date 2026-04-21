// First attempt at an object 
const rep = { x: 0 }
const counter = {
    get: () => rep.x, 
    inc: function() { return rep.x = rep.x + 1}
}

const counterClient = function(counter) {
    counter.inc(); 
    counter.inc(); 
    return counter.get();
}

const res = counterClient(counter)
res 

// Classes 
const counterClassBroken = function() {
    return {
        get: () => rep.x, 
        inc: () => rep.x = rep.x + 1
    }
}

const c1 = counterClassBroken()
const c2 = counterClassBroken()
const res1 = counterClient(c1)
const res2 = counterClient(c2)
res1
res2
 
// The same rep is being modified by each counter object 
// Our counter object can be entirely bypassed 
rep.x = 100
rep.x = rep.x + 100

// Second attempt at proper encapsulation 
const counterClass = function(rep) {
    return {
        get: function() { return rep.x }, 
        inc: function() { return rep.x = rep.x + 1 }
    }
}

const newCounter = function() {
    const rep = { x: 0 }
    return counterClass(rep)
}

// Extending classes with new methods 
// class resetCounter extends Counter 
const resetCounterClass = function(rep) {
    const _super = counterClass(rep)
    return {
        get: _super.get,
        inc: _super.inc,
        reset: () => rep.x = 0 
    }
}

const newResetCounter = function() {
    const rep = { x: 0 }
    return resetCounterClass(rep)
}

const e1 = newCounter()
const e2 = newResetCounter()
console.log(e1.inc())
console.log(e2.inc())
const res3 = counterClient(e1)
const res4 = counterClient(e2)
res3 
res4
const res5 = e2.reset()
res5
// console.log(e1.rep.x)

// Extending classes with new fields 
const backupCounterClass = function(rep) {
    const _super = resetCounterClass(rep)
    return {
        get: _super.get,
        get_backup: () => rep.y, 
        inc: _super.inc,
        reset: _super.reset,
        backup: () => (rep.y = rep.x, _super.reset)
        // backup: function() { rep.y = rep.x; return this.reset()}
    }
}

const newBackupCounter = function() {
    const rep = { x: 0, y: 0 }
    return backupCounterClass(rep)
}

const e3 = newBackupCounter()
e3.inc()
e3.inc()
e3.backup()
const res6 = e3.get()
res6 
const res7 = e3.get_backup()
res7

// Open recursion: defining methods in terms of previously defined methods 
// Revisiting counterClass 
// Original counterClass takes a representation and returns an object, i.e. the object's address 
// New counterClass takes an address directly 
const updateCounterClass = function(_this) {
    _this.x = 0; 
    _this.set = (i) => _this.x = i;
    _this.get = () => _this.x; 
    _this.inc = () => _this.set(_this.get()+1); // Defining inc in terms of get and set 
    return _this
}

// Notice that the body of setCounterClass does not allocate any memory 

// Only newSetCounter allocates memory, once 
const newSetCounter = function() {
    const _this = {
        x: undefined, 
        set: undefined, 
        get: undefined,
        inc: undefined
    }
    return updateCounterClass(_this)
}

const e7 = newSetCounter()
e7.set(10)
e7.inc()
e7