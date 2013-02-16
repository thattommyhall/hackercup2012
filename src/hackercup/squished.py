import math
import random
def partitions(somestring,valid_chars,sofar):
	l = len(somestring)
	if l == 0:
		yield sofar
	one = somestring[0]
	if one in valid_chars:
		partitions(somestring[1:],valid_chars,[one]+sofar)
	if l >= 2:
		two = somestring[1]
		if one+two in valid_chars:
			partitions(somestring[2:],valid_chars,[one+two]+sofar)
	if l >= 3:
		three = somestring[2]
		if one+two+three in valid_chars:
			partitions(somestring[3:],valid_chars,[one+two+three] + sofar)


test_str = ''
for i in range(100):
	test_str += str(int(math.ceil(random.random() * 10000000000)))

print(len(test_str))
count = 0
for i in partitions('1111111111111222222',['1','2','12'],[]):
	print i
	count += 1
	

print(count)
	
	
