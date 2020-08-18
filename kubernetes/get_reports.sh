#!/bin/bash
export HOME_TESTS=$WORKDIR
kubectl get deployments | awk 'NR>1 {print $1}' > $HOME_TESTS/testids/deployments.txt
kubectl get pods | awk 'NR>1 {print $1}' > $HOME_TESTS/testids/pods.txt
cat $HOME_TESTS/testids/pods.txt
echo "*******************"
  
while read -r pod; 
do 
    echo "processing pod: $pod"
    kubectl cp $pod:/salesforce/rpt_ids.txt $HOME_TESTS/testids
    prototype=`head -1 $HOME_TESTS/testids/rpt_ids.txt`
    echo "prototype = $prototype"
    sed '1d' $HOME_TESTS/testids/rpt_ids.txt > $HOME_TESTS/testids/tmpids 
    mv $HOME_TESTS/testids/tmpids $HOME_TESTS/testids/rpt_ids.txt
    if [ ! -s $HOME_TESTS/testids/rpt_ids.txt ] ; then
        echo "XXXXX==> NO REPORTS FOR pod: $pod"
            rm -f $HOME_TESTS/testids/tmpids
        continue
    fi
    rm -f $HOME_TESTS/testids/tmpids
    cat $HOME_TESTS/testids/rpt_ids.txt
    while read -r testid
    do
        test_id=`echo $testid | tr -d '\r'` 
        kubectl cp $pod:/salesforce/raw_"$prototype"_"$test_id".csv $HOME_TESTS/reports/$prototype
        if [ $? -ne 0 ] ; then
            echo "XXXXX==> NO REPORT FOR testid: $testid"
        fi
    done < $HOME_TESTS/testids/rpt_ids.txt
    rm -f $HOME_TESTS/testids/rpt_ids.txt
done < $HOME_TESTS/testids/pods.txt
rm -f $HOME_TESTS/testids/pods.txt
#kubectl delete deployment salesforce
rm -f $HOME_TESTS/testids/deployments.txt



