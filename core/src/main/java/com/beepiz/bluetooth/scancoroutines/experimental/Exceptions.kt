package com.beepiz.bluetooth.scancoroutines.experimental

import android.bluetooth.le.ScanCallback

/**
 * @property errorCode Possible values: [ScanCallback.SCAN_FAILED_ALREADY_STARTED],
 * [ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED],
 * [ScanCallback.SCAN_FAILED_INTERNAL_ERROR],
 * [ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED],
 * [SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES],
 * [SCAN_FAILED_SCANNING_TOO_FREQUENTLY]. There may be some undocumented additional errorCodes.
 */
class ScanFailedException(val errorCode: Int) : Exception("Scan failed. errorCode = $errorCode")
