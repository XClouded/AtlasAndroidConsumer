package atlasapp.common;


import com.google.android.gms.common.ConnectionResult;
import android.content.Context;
import android.content.res.Resources;
import atlasapp.section_appentry.R;



/**
 * Map error codes to error messages.
 */
public class LocationServiceErrorMessages {

    // Don't allow instantiation
    private LocationServiceErrorMessages() {}

    public static String getErrorString(Context context, int errorCode) {

        // Get a handle to resources, to allow the method to retrieve messages.
        Resources mResources = context.getResources();

        // Define a string to contain the error message
        String errorString;

        // Decide which error message to get, based on the error code.
        switch (errorCode) {
            case ConnectionResult.DEVELOPER_ERROR:
                errorString = mResources.getString(R.string.connection_error_misconfigured);
                break;

            case ConnectionResult.INTERNAL_ERROR:
                errorString = mResources.getString(R.string.connection_error_internal);
                break;

            case ConnectionResult.INVALID_ACCOUNT:
                errorString = mResources.getString(R.string.connection_error_invalid_account);
                break;

            case ConnectionResult.LICENSE_CHECK_FAILED:
                errorString = mResources.getString(R.string.connection_error_license_check_failed);
                break;

            case ConnectionResult.NETWORK_ERROR:
                errorString = mResources.getString(R.string.connection_error_network);
                break;

            case ConnectionResult.RESOLUTION_REQUIRED:
                errorString = mResources.getString(R.string.connection_error_needs_resolution);
                break;

            case ConnectionResult.SERVICE_DISABLED:
                errorString = mResources.getString(R.string.connection_error_disabled);
                break;

            case ConnectionResult.SERVICE_INVALID:
                errorString = mResources.getString(R.string.connection_error_invalid);
                break;

            case ConnectionResult.SERVICE_MISSING:
                errorString = mResources.getString(R.string.connection_error_missing);
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                errorString = mResources.getString(R.string.connection_error_outdated);
                break;

            case ConnectionResult.SIGN_IN_REQUIRED:
                errorString = mResources.getString(
                        R.string.connection_error_sign_in_required);
                break;

            default:
                errorString = mResources.getString(R.string.connection_error_unknown);
                break;
        }

        // Return the error message
        return errorString;
    }
}
