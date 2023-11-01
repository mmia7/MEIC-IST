#!/bin/zsh
cd ..
mkdir -p compiled images
setopt extended_glob

dates=('SEP/20/2018' 'OCT/9/2099' 'APR/19/2001')

# ############ Compile source transducers ############

trans1=pt2en.fst

res_trans=en2pt.fst

# ############ CORE OF THE PROJECT  ############

echo "Inverting pt2en to en2pt.fst"
fstinvert compiled/$trans1 > compiled/$res_trans



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
