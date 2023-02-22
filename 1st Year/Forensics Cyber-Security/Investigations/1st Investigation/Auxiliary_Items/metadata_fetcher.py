from PIL import Image
im = Image.open("Relativity.gif")
file = open("Metadata","wb")
file.write(im.info['comment'])
file.close()