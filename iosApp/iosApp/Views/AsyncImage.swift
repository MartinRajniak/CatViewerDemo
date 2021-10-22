import Combine
import SwiftUI

struct AsyncImage<Content: View>: View {
    @StateObject private var store: AsyncImageStore

    private let content: (Image) -> Content

    init(
      _ imagePublisher: AnyPublisher<UIImage, Error>,
      @ViewBuilder content: @escaping (Image) -> Content
    ) {
        self.content = content

        _store = .init(
          wrappedValue: AsyncImageStore(imagePublisher: imagePublisher)
        )
    }

    var body: some View {
        VStack {
            if let image = store.image {
                content(
                  Image(uiImage: image)
                )
            } else {
                ProgressView()
            }
        }
        .onAppear(perform: store.load)
    }
}

class AsyncImageStore: ObservableObject {
    @Published var image: UIImage?

    private let imagePublisher: AnyPublisher<UIImage, Error>

    private var cancellable: AnyCancellable?

    init(imagePublisher: AnyPublisher<UIImage, Error>) {
        self.imagePublisher = imagePublisher
    }

    func load() {
        cancellable = imagePublisher
            .map { $0 }
            .replaceError(with: nil)
            .receive(on: DispatchQueue.main)
            .assign(to: \.image, on: self)
    }
}
