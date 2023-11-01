#!/bin/zsh

mkdir -p compiled images
setopt extended_glob


# ############ Compile source transducers ############

for i in sources/*.txt tests/*.txt; do
	echo "Compiling: $i"
    fstcompile --isymbols=syms.txt --osymbols=syms.txt $i | fstarcsort > compiled/$(basename $i ".txt").fst
done

# ############ CORE OF THE PROJECT  ############

echo "\nCreating mix2numerical.fst:" 
echo "Concatenating mmm2mm.fst to copy_day_year2out.fst..."
fstconcat compiled/mmm2mm.fst compiled/copy_day_year2out.fst > compiled/mix2numerical.fst

echo "\nCreating pt2en.fst:" 
echo "Concatenating month_pt2en.fst to copy_day_year2out.fst..."
fstconcat compiled/month_pt2en.fst compiled/copy_day_year2out.fst > compiled/pt2en.fst

echo "\nCreating en2pt.fst:"
echo "Inverting pt2en.fst to en2pt.fst..."
fstinvert compiled/pt2en.fst > compiled/en2pt.fst

echo "\nCreating datenum2text.fst:"
echo "Concatenating month.fst to separator.fst to day.fst to comma.fst to year.fst..."
fstconcat compiled/month.fst compiled/separator.fst > compiled/month_separated.fst
fstconcat compiled/month_separated.fst compiled/day.fst > compiled/monthday.fst
fstconcat compiled/monthday.fst compiled/comma.fst > compiled/monthdaycomma.fst
fstconcat compiled/monthdaycomma.fst compiled/year.fst > compiled/datenum2text.fst

echo "\nCreating mix2text.fst:"
echo "Composing mix2numerical.fst with datenum2text.fst..."
fstcompose compiled/mix2numerical.fst compiled/datenum2text.fst > compiled/mix2text.fst

echo "\nCreating date2text.fst:"
echo "Uniting mix2text.fst with datenum2text.fst..."
fstunion compiled/mix2text.fst compiled/datenum2text.fst > compiled/date2text.fst

# ############ generate PDFs  ############

echo "\nStarting to generate PDFs"
for i in compiled/*.fst; do
	echo "Creating image: images/$(basename $i '.fst').pdf"
   fstdraw --portrait --isymbols=syms.txt --osymbols=syms.txt $i | dot -Tpdf > images/$(basename $i '.fst').pdf
done

# ##################### TESTS ########################

fst2word() { awk '{if(NF>=3){printf("%s",$3)}}END{printf("\n")}' }

to_test=('mix2numerical.fst' 'en2pt.fst' 'datenum2text.fst' 'mix2text.fst' 'date2text.fst')

for transducer in $to_test; do

    echo "\n=========================================================="
    echo "Testing transducer: $transducer"
    echo "==========================================================\n"

    for w in tests/t-*.txt; do
        res=$(cat $w | fstcompile --isymbols=syms.txt --osymbols=syms.txt | fstarcsort |
                            fstcompose - compiled/$transducer | fstshortestpath | fstproject --project_type=output |
                            fstrmepsilon | fsttopsort | fstprint --acceptor --isymbols=syms.txt | fst2word)
        if [[ ! -z "$res" ]] then
            echo "Testing with file: $w..." 
            date=$(cat $w | fst2word)
            echo "$date = $res\n"
        fi
    done
done

echo "\n=========================================================="
echo "Creating final output files example with date2text.fst"
echo "==========================================================\n"

echo "Creating output files of test files after being fed to date2text:"
for w in compiled/t-*.fst; do
    # ignore repeated outputs
    if [[ $w != *t-*-out.fst ]]; then 
        fstcompose $w compiled/date2text.fst | fstshortestpath | fstproject --project_type=output |
            fstrmepsilon | fsttopsort > compiled/$(basename $w ".fst")-out.fst
    fi
done
echo "Creating pdf files of the test outputs..."
for i in compiled/t-*-out.fst; do
	echo "Creating image: images/$(basename $i '.fst').pdf"
   fstdraw --portrait --isymbols=syms.txt --osymbols=syms.txt $i | dot -Tpdf > images/$(basename $i '.fst').pdf
done

echo "The end!"
