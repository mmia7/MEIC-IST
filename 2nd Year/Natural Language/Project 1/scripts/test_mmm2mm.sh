#!/bin/zsh
cd ..
mkdir -p compiled images
setopt extended_glob

txt=mmm2mm.txt
trans=mmm2mm.fst
months=('JAN' 'FEV' 'MAR' 'ABR' 'APR' 'MAI' 'MAY' 'JUN' 'JUL' 'AGO' 'AUG' 'SET' 'SEP' 'OUT' 'OCT' 'NOV' 'DEZ' 'DEC' )

# ############ Compile source transducers ############
echo "Compiling: $txt"
fstcompile --isymbols=syms.txt --osymbols=syms.txt sources/$txt | fstarcsort > compiled/$trans


# ############ CORE OF THE PROJECT  ############






# ############ generate PDFs  ############
echo "Starting to generate PDFs"
for i in compiled/*.fst; do
	echo "Creating image: images/$(basename $i '.fst').pdf"
   fstdraw --portrait --isymbols=syms.txt --osymbols=syms.txt $i | dot -Tpdf > images/$(basename $i '.fst').pdf
done


echo "\n========================================================="
echo "Testing mmm2mm.fst"
echo "=========================================================="


#1 - present the output as an acceptor
echo "\n***********************************************************"
echo "Testing JAN DEC DEZ FEB (w/ Output as an Acceptor"
echo "***********************************************************"

for w in "JAN" "DEC" "DEZ" "FEB"; do
    echo "\t $w"
    ./scripts/date2fst.py $w | fstcompile --isymbols=syms.txt --osymbols=syms.txt | fstarcsort |
                     fstcompose - compiled/$trans | fstshortestpath | fstproject --project_type=output |
                     fstrmepsilon | fsttopsort | fstprint --acceptor --isymbols=syms.txt
done



#1 - presents the output with the tokens concatenated

fst2word() { awk '{if(NF>=3){printf("%s",$3)}}END{printf("\n")}' }

echo "\n***********************************************************"
echo "Testing the conversion of all months"
echo "***********************************************************"
for w in $months; do
    res=$(./scripts/date2fst.py $w | fstcompile --isymbols=syms.txt --osymbols=syms.txt | fstarcsort |
                       fstcompose - compiled/$trans | fstshortestpath | fstproject --project_type=output |
                       fstrmepsilon | fsttopsort | fstprint --acceptor --isymbols=syms.txt | fst2word)
    echo "$w = $res"
done

echo "\nThe end"
