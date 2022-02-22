//
//  CallEventHandler.swift
//  flutter_ring_kit
//
//  Created by user201135 on 2/22/22.
//

import Foundation
import Flutter

class CallEventHandler: NSObject, FlutterStreamHandler{
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        return nil
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        return nil
    }
}
