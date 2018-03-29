package com.example.kkccbd;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.HashMap;

/**
 * Created by ???, on 15/11/17.
 */

/*
* 1000 = apple
* 1001 = banana
* 1002 = chikku
* 1003 = custard apple
* 1004 = grape
* 1005 = mango
* 1006 = orange
*
* 2000 = camel
* 2001 = tiger
* 2002 = elephant
* 2003 = cat
* 2004 = cow
* 2005 = buffalo
* 2006 = ox
* 2007 = lion
* 2008 = cheetah
* 2009 = dog
*
* 3000 = duck
* 3001 = pigeon
* 3002 = peacock
* 3003 = parrot
*
* 4000 = rose
* 4001 = hibiscus
* 4002 = lotus
* 4003 = sunflower
*
* 5000 = red
* 5001 = blue
* 5002 = white
* 5003 = black
* 5004 = yellow
* 5005 = orange
* 5006 = purple
* 5007 = pink
* 5008 = brown
 */

public class Util {

    public static final String EXTRA_MESSAGE = "com.example.kkccbd.MESSAGE";

    public static final String COGNITO_POOL_ID = "us-east-1:b47c21bb-9046-4551-a07f-2c0a277821cc";

    public static final String COGNITO_POOL_REGION = "us-east-1";

    public static final String BUCKET_NAME = "kannadakali";

    public static final String BUCKET_REGION = "ap-south-1";

    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;

    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    COGNITO_POOL_ID,
                    Regions.fromName(COGNITO_POOL_REGION));
        }
        return sCredProvider;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            sS3Client.setRegion(Region.getRegion(Regions.fromName(BUCKET_REGION)));
        }
        return sS3Client;
    }

    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }

        return sTransferUtility;
    }

//    public static boolean isNetwork (Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null)
//        {
//            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
//            if(netInfos != null)
//                if(netInfos.isConnected())
//                    return true;
//        }
//        return false;
//    }

    public static boolean isNetwork (Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}