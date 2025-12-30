var obj1=require("./01.js");
var obj2=require("./02.js");
require("./style.css");

console.log("abc.html输出"+obj2.add(3,2));
obj1.write(obj2.add(3,2));