from PIL import Image
import os, sys, struct

if not os.path.exists("output"):
    os.mkdir("output")

map_image = sys.argv[1]
map_image = os.path.expanduser(map_image)
im = Image.open(map_image)

out_name = os.path.basename(map_image)
#chop off the file extension "bw.png"
out_name = out_name[:-4]
out_name += ".jbm"
out = open("output/" + out_name, "wb")

max_x = im.size[0]
max_y = im.size[1]

mx = struct.pack('>i', max_x)
my = struct.pack('>i', max_y)
out.write(mx)
out.write(my)

y = 0
while y < max_y:
    bit = 0
    byte_value = 0
    x = 0
    while x < max_x:
        p = im.getpixel( (x,y) )
        if p == 1:
            byte_value += p * 2**bit
        bit += 1
        if bit == 8:
            data = struct.pack('i', byte_value)
            out.write(data)
            bit = 0
            byte_value = 0
        x += 4
    y += 4

out.close()

