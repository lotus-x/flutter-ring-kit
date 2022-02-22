import Flutter
import UIKit

@available(iOS 10.0, *)
public class SwiftFlutterRingKitPlugin: NSObject, FlutterPlugin {
    private var callManager: CallManager? = nil
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(
            name: "com.oryn.lotus/flutter_ring_kit",
            binaryMessenger: registrar.messenger()
        )
        let instance = SwiftFlutterRingKitPlugin()
        let callerEventChannel = FlutterEventChannel(
            name: "com.oryn.lotus/flutter_ring_kit/caller_callback",
            binaryMessenger: registrar.messenger()
        )
        callerEventChannel.setStreamHandler(CallEventHandler())
        // init call manager
        instance.callManager = CallManager()
        // add method call handler
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        // check method
        switch call.method {
        case "ringCall":
            self.callManager?.ringCall()
            result(true)
        case "checkLaunchedUponCall":
            result(false)
        case "getLaunchedCallerId":
            result("")
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
