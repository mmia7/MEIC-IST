#!/bin/zsh
cd ..
mkdir -p compiled images
setopt extended_glob

dates=('09/20/2018' '10/9/2099' '4/19/2001' '9/15/2055')

# ############ Compile source transducers ############

txt1=month.txt
trans1=month.fst

echo "Compiling: $txt1"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt1 | fstarcsort > compiled/$trans1

txt2=separator.txt
trans2=separator.fst

echo "Compiling: $txt2"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt2 | fstarcsort > compiled/$trans2

txt3=day.txt
trans3=day.fst

echo "Compiling: $txt3"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt3 | fstarcsort > compiled/$trans3

txt4=comma.txt
trans4=comma.fst

echo "Compiling: $txt4"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt4 | fstarcsort > compiled/$trans4

txt5=year.txt
trans5=year.fst

echo "Compiling: $txt5"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt5 | fstarcsort > compiled/$trans5

res_trans=datenum2text.fst

# ############ CORE OF THE PROJECT  ############

echo "Concatenating $trans1 to $trans2 to $trans3 to $trans4 to $trans5"
fstconcat compiled/$trans1 compiled/$trans2 > compiled/month_separated.fst
fstconcat compiled/month_separated.fst compiled/$trans3 > compiled/monthday.fst
fstconcat compiled/monthday.fst compiled/$trans4 > compiled/monthdaycomma.fst
fstconcat compiled/monthdaycomma.fst compiled/$trans5 > compiled/$res_trans



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
