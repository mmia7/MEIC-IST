#!/bin/zsh
cd ..
mkdir -p compiled images
setopt extended_glob

dates=('SET/20/2018' 'OUT/9/2099' 'ABR/19/2001' 'MAY/12/2088' 'MAI/12/2088')

# ############ Compile source transducers ############

txt1=mix2numerical.txt
trans1=mix2numerical.fst

txt2=datenum2text.txt
trans2=datenum2text.fst

res_trans=mix2text.fst

# ############ CORE OF THE PROJECT  ############

echo "Compose mix2numerical.fst with datenum2text.fst"
fstcompose compiled/$trans1 compiled/$trans2 > compiled/$res_trans



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
echo "Testing the conversion for 5 dates"
echo "***********************************************************"

#fst to test
for w in $dates; do
    res=$(./scripts/date2fst.py $w | fstcompile --isymbols=syms.txt --osymbols=syms.txt | fstarcsort |
                       fstcompose - compiled/$res_trans | fstshortestpath | fstproject --project_type=output |
                       fstrmepsilon | fsttopsort | fstprint --acceptor --isymbols=syms.txt | fst2word)
    echo "$w = $res"
done

echo "\nThe end"
