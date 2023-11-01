#!/bin/zsh
cd ..
mkdir -p compiled images
setopt extended_glob
# ############ Compile source transducers ############

txt=year.txt
trans=year.fst

echo "Compiling: $txt"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt | fstarcsort > compiled/$trans

res_trans=year.fst

# ############ CORE OF THE PROJECT  ############


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
echo "Testing the conversion of all years"
echo "***********************************************************"

#fst to test
for w in {2001..2099}; do
    res=$(./scripts/date2fst.py $w | fstcompile --isymbols=syms.txt --osymbols=syms.txt | fstarcsort |
                       fstcompose - compiled/$res_trans | fstshortestpath | fstproject --project_type=output |
                       fstrmepsilon | fsttopsort | fstprint --acceptor --isymbols=scripts/syms-out.txt | fst2word)
    echo "$w = $res"
done

echo "\nThe end"
