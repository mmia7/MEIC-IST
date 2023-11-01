#!/bin/zsh
cd ..
mkdir -p compiled images
setopt extended_glob

dates=('SET/20/2018' 'OUT/9/2099' 'ABR/19/2001')

# ############ Compile source transducers ############

txt1=month_pt2en.txt
trans1=month_pt2en.fst

echo "Compiling: $txt1"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt1 | fstarcsort > compiled/$trans1

txt2=copy_day_year2out.txt
trans2=copy_day_year2out.fst

echo "Compiling: $txt2"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt2 | fstarcsort > compiled/$trans2

res_trans=pt2en.fst

# ############ CORE OF THE PROJECT  ############

echo "Concatenating month_pt2en.fst to copy_day_year2out.fst"
fstconcat compiled/$trans1 compiled/$trans2 > compiled/$res_trans



# ############ generate PDFs  ############
echo "Starting to generate PDFs"
for i in compiled/*.fst; do
	echo "Creating image: images/$(basename $i '.fst').pdf"
   fstdraw --portrait --isymbols=syms.txt --osymbols=syms.txt $i | dot -Tpdf > images/$(basename $i '.fst').pdf
done


echo "\n========================================================="
echo "Testing $res_trans"
echo "=========================================================="

#1 - presents the output with the tokens concatenated
fst2word() { awk '{if(NF>=3){printf("%s",$3)}}END{printf("\n")}' }

echo "\n***********************************************************"
echo "Testing the conversion for 3 dates"
echo "***********************************************************"

#fst to test
for w in $dates; do
    res=$(./scripts/date2fst.py $w | fstcompile --isymbols=syms.txt --osymbols=syms.txt | fstarcsort |
                       fstcompose - compiled/$res_trans | fstshortestpath | fstproject --project_type=output |
                       fstrmepsilon | fsttopsort | fstprint --acceptor --isymbols=syms.txt | fst2word)
    echo "$w = $res"
done

echo "\nThe end"
