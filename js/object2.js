// Counter 
// Internal representation/fields 
const counterClass = function(rep) {
    return {
        inc: () => (rep.x = rep.x + 1), 
        get: () => rep.x
    }
}

const newCounter = function() {
    const rep = { x: 0 }
    return counterClass(x)
}

// Encapsulation 
// resetCounterClass extends counterClass 
// Extending classes with methods 
// Overriding existing methods in classes 
// Extending classes with fields 
// y: 0, backup 

const backupCounterClass = function(rep) {
    const _super = counterClass(rep)
    return {
        inc: () => (rep.x = rep.x + 2),  
        get: _super.get,
        backup: () => rep.y = rep.x, 
        get_y: () => rep.y,
        set: (i) => rep.x = i
    }
}

const newBackupCounter = function() {
    const rep = { x: 0, y: 0 }
    return backupCounterClass(rep)
}

const resetBackupCounterClass = function(rep) {
    const _super = backupCounterClass(rep)
    return {
        inc: _super.inc,
        get: _super.get,
        backup: _super.backup,
        get_y: _super.get_y,
        set: _super.set, 
        reset: _super.set(0)
    }
}

const newResetBackupCounter = function() {
    const rep = { x: 0, y: 0 }
    return resetBackupCounterClass(rep)
}

const c = newResetBackupCounter()
c.inc()
c.inc()
const res = c.get()
res 
const res2 = c.get()
res2
c.backup()
const res3 = c.get_y()
res3

// Open recursion: using methods in the same class to define other methods 
const orCounterClass = function(rep,_this) {
    _this.inc = () => _this.set(rep.x+1);
    _this.get = () => rep.x;
    _this.set = (i) => rep.x = i; 
    return _this
}

const newOrCounterClass = function() {
    const c = { 
        inc: undefined, 
        get: undefined, 
        set: undefined, 
    }
    const rep = { x: 0 }
    return orCounterClass(rep,c)
}

// Extending classes in this style 
const orResetCounterClass = function(rep,_this) {
        _this.reset = () => rep.x = 0 
        return _this
}

const newOrResetCounterClass = function() {
    const c = { 
        inc: undefined, 
        get: undefined, 
        set: undefined,     
        reset: undefined 
    }
    const rep = { x: 0 }
    orCounterClass(rep,c)
    orResetCounterClass(rep,c)
    return c
}

const r = newOrResetCounterClass()
r.inc()
r.inc()
console.log(r.get())
r.set(10)
console.log(r.get())
r.reset()
console.log(r.get())