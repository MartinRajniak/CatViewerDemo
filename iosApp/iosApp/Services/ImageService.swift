import SwiftUI
import Combine
import Foundation

class ImageService {
    static let shared = ImageService()
    
    private init() {
        // Singleton
    }
}

extension ImageService {
    func getImage(url: String) -> AnyPublisher<UIImage, Error> {
        guard let url = URL(string: url) else {
            return AnyPublisher(
                Fail<UIImage, Error>(error: URLError(.badURL))
            )
        }
        
        return URLSession.shared
            .dataTaskPublisher(for: url)
            .tryMap { result -> UIImage in
                return UIImage(data: result.data) ?? UIImage()
            }
            .eraseToAnyPublisher()
    }
}
