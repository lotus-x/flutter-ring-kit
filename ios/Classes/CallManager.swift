//
//  CallManager.swift
//  flutter_ring_kit
//
//  Created by user201135 on 2/13/22.
//

import Foundation
import CallKit

@available(iOS 10.0, *)
class CallManager: NSObject{
    private let provider: CXProvider
    
    override init() {
        self.provider = CXProvider(
            configuration: CallManager.providerConfiguration
        )
    }
    
    static var providerConfiguration: CXProviderConfiguration = {
        let appName = Bundle.main.infoDictionary?[kCFBundleNameKey as String] as! String
        var configuration: CXProviderConfiguration
        if #available(iOS 14.0, *) {
            configuration = CXProviderConfiguration.init()
        } else {
            configuration = CXProviderConfiguration(localizedName: appName)
        }
        
        configuration.supportsVideo = true
        configuration.maximumCallsPerCallGroup = 1
        configuration.maximumCallGroups = 1;
        configuration.supportedHandleTypes = [.generic]
        
        if #available(iOS 11.0, *) {
            configuration.includesCallsInRecents = false
        }
        
        return configuration
    }()
    
    func ringCall(){
        let callUpdate = CXCallUpdate()
        provider.reportNewIncomingCall(
            with: UUID(),
            update: callUpdate
        ) {error in
            print("test")
            print(error ?? "call rang")
        }
    }
    
    func provider(_ provider: CXProvider, perform action: CXAnswerCallAction) {
        print(action)
        // configure audio session
        action.fulfill()
    }
}
