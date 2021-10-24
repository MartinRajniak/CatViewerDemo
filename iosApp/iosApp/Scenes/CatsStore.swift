import Combine
import shared

class CatsStore: ObservableObject {
    private let viewModel = CatsViewModel()
    
    @Published var cats: [Cat] = []
    @Published var hasNextPage: Bool = false
    
    func fetch() {
        viewModel.cats.watch { cats in
            guard let list = cats?.compactMap({ $0 as? Cat}) else {
                return
            }
            self.cats = list
            self.hasNextPage = true // FIXME: should check if there is more data
        }
    }
    
    func onScrolledToTheEnd() {
        viewModel.onScrolledToTheEnd()
    }
}
